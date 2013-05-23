package wisematches.client.android.data.service;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager;
import wisematches.client.android.data.DataRequestManager;
import wisematches.client.android.data.model.Id;
import wisematches.client.android.data.model.Language;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.model.scribble.ActiveGames;
import wisematches.client.android.data.model.scribble.WaitingGames;
import wisematches.client.android.data.service.operation.person.RegisterPlayerOperation;
import wisematches.client.android.data.service.operation.person.SignInPlayerOperation;
import wisematches.client.android.data.service.operation.scribble.ActiveGamesOperation;
import wisematches.client.android.data.service.operation.scribble.CreateGameOperation;
import wisematches.client.android.data.service.operation.scribble.ProcessProposalOperation;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class JSONRequestManager extends RequestManager implements DataRequestManager {
	public static final int REQUEST_TYPE_AUTH = 1;
	public static final int REQUEST_TYPE_REGISTER = 2;

	public static final int REQUEST_TYPE_CREATE_GAME = 3;

	public static final int REQUEST_TYPE_ACTIVE_GAMES = 4;
	public static final int REQUEST_TYPE_WAITING_GAMES = 5;
	public static final int REQUEST_TYPE_PROCESS_PROPOSAL = 6;

	public static final String BUNDLE_EXTRA_RESPONSE_TYPE = "wisematches.client.extra.response.type";
	public static final String BUNDLE_EXTRA_RESPONSE_TYPE_LIST = "wisematches.client.extra.response.type.list";
	public static final String BUNDLE_EXTRA_RESPONSE_TYPE_PRIMITIVE = "wisematches.client.extra.response.type.primitive";

	public JSONRequestManager(Context context) {
		super(context, JSONRequestService.class);
	}

	@Override
	public void authenticate(String username, String password, DataResponse<Personality> response) {
		final Request request = new Request(REQUEST_TYPE_AUTH);
		request.setMemoryCacheEnabled(false);
		if (username == null && password == null) {
			request.put(SignInPlayerOperation.PARAM_VISITOR, true);
		} else {
			request.put(SignInPlayerOperation.PARAM_USERNAME, username);
			request.put(SignInPlayerOperation.PARAM_PASSWORD, password);
		}

		execute(request, new TheRequestListener<>(response));
	}

	@Override
	public void register(String nickname, String email, String password, String confirm, String language, String timezone, DataResponse<Personality> response) {
		final Request request = new Request(REQUEST_TYPE_REGISTER);
		request.setMemoryCacheEnabled(false);
		request.put(RegisterPlayerOperation.PARAM_USERNAME, nickname);
		request.put(RegisterPlayerOperation.PARAM_EMAIL, email);
		request.put(RegisterPlayerOperation.PARAM_PASSWORD, password);
		request.put(RegisterPlayerOperation.PARAM_CONFIRM, confirm);
		request.put(RegisterPlayerOperation.PARAM_LANGUAGE, language);
		request.put(RegisterPlayerOperation.PARAM_TIMEZONE, timezone);

		execute(request, new TheRequestListener<>(response));
	}

	@Override
	public void getActiveGames(long pid, DataResponse<ActiveGames> response) {
		final Request request = new Request(REQUEST_TYPE_ACTIVE_GAMES);
		request.put(ActiveGamesOperation.PLAYER_ID, pid);

		execute(request, new TheRequestListener<>(response));
	}

	@Override
	public void getWaitingGames(DataResponse<WaitingGames> response) {
		final Request request = new Request(REQUEST_TYPE_WAITING_GAMES);
		execute(request, new TheRequestListener<>(response));
	}

	@Override
	public void processWaitingGame(long proposalId, boolean accept, DataResponse<Id> response) {
		final Request request = new Request(REQUEST_TYPE_PROCESS_PROPOSAL);
		request.put(ProcessProposalOperation.PARAM_ID, proposalId);
		request.put(ProcessProposalOperation.PARAM_TYPE, accept);
		execute(request, new TheRequestListener<>(response));
	}

	@Override
	public void createNewGame(String title, Language language, int timeout, String createTab, String robotType, int opponentsCount, DataResponse<Id> response) {
		final Request request = new Request(REQUEST_TYPE_CREATE_GAME);
		request.put(CreateGameOperation.PARAM_TITLE, title);
		request.put(CreateGameOperation.PARAM_LANGUAGE, language.getCode());
		request.put(CreateGameOperation.PARAM_TIMEOUT, timeout);
		request.put(CreateGameOperation.PARAM_TAB, createTab);
		request.put(CreateGameOperation.PARAM_ROBOT_TYPE, robotType);
		request.put(CreateGameOperation.PARAM_OPPONENTS_COUNT, opponentsCount);

		execute(request, new TheRequestListener<>(response));
	}

	private class TheRequestListener<T> implements RequestManager.RequestListener {
		private final DataResponse<T> response;

		protected TheRequestListener(DataResponse<T> response) {
			this.response = response;
		}

		@Override
		@SuppressWarnings("unchecked")
		public void onRequestFinished(Request request, Bundle resultData) {
			if (resultData == null) {
				Log.d("WM", "Finished: empty");
				response.onSuccess(null);
			} else {
				Log.d("WM", "Finished: not empty");
				final String type = resultData.getString(BUNDLE_EXTRA_RESPONSE_TYPE);
				if (type == null) {
					response.onSuccess(null);
				} else if (type.equals(BUNDLE_EXTRA_RESPONSE_TYPE_LIST)) {
					response.onSuccess((T) resultData.getParcelableArrayList(BUNDLE_EXTRA_RESPONSE_TYPE_LIST));
				} else if (type.equals(BUNDLE_EXTRA_RESPONSE_TYPE_PRIMITIVE)) {
					response.onSuccess((T) resultData.getParcelable(BUNDLE_EXTRA_RESPONSE_TYPE_PRIMITIVE));
				}
			}
		}

		@Override
		public void onRequestDataError(Request request) {
			Log.d("WM", "Finished: onDataError");
			response.onDataError();
		}

		@Override
		public void onRequestConnectionError(Request request, int statusCode) {
			Log.d("WM", "Finished: onConnectionError " + statusCode);
			response.onConnectionError(statusCode);
		}

		@Override
		public void onRequestCustomError(Request request, Bundle resultData) {
			Log.d("WM", "Finished: onFailure");
			response.onFailure(resultData.getString("code"), resultData.getString("message"));
		}
	}
}