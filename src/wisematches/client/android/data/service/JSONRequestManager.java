package wisematches.client.android.data.service;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager;
import wisematches.client.android.data.DataRequestManager;
import wisematches.client.android.data.model.Id;
import wisematches.client.android.data.model.Language;
import wisematches.client.android.data.model.info.InfoPage;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.model.scribble.*;
import wisematches.client.android.data.service.operation.dict.LoadWordEntryOperation;
import wisematches.client.android.data.service.operation.info.LoadInfoPageOperation;
import wisematches.client.android.data.service.operation.person.RegisterPlayerOperation;
import wisematches.client.android.data.service.operation.person.SignInPlayerOperation;
import wisematches.client.android.data.service.operation.scribble.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class JSONRequestManager extends RequestManager implements DataRequestManager {
	private static int requestInIndex = 1;

	public static final int REQUEST_TYPE_AUTH = requestInIndex++;
	public static final int REQUEST_TYPE_REGISTER = requestInIndex++;

	public static final int REQUEST_TYPE_OPEN_GAME = requestInIndex++;
	public static final int REQUEST_TYPE_CREATE_GAME = requestInIndex++;

	public static final int REQUEST_TYPE_ACTIVE_GAMES = requestInIndex++;
	public static final int REQUEST_TYPE_WAITING_GAMES = requestInIndex++;
	public static final int REQUEST_TYPE_PROCESS_PROPOSAL = requestInIndex++;

	public static final int REQUEST_TYPE_BOARD_ACTION = requestInIndex++;

	public static final int REQUEST_TYPE_DICT_WORD = requestInIndex++;

	public static final int REQUEST_TYPE_LOAD_INFO = requestInIndex++;

	public static final String BUNDLE_EXTRA_RESPONSE_TYPE = "wisematches.client.extra.response.type";
	public static final String BUNDLE_EXTRA_RESPONSE_TYPE_LIST = "wisematches.client.extra.response.type.list";
	public static final String BUNDLE_EXTRA_RESPONSE_TYPE_PRIMITIVE = "wisematches.client.extra.response.type.primitive";

	public JSONRequestManager(Context context) {
		super(context, JSONRequestService.class);
	}

	@Override
	public void login(String username, String password, DataResponse<Personality> response) {
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
		request.put(ActiveGamesOperation.PARAM_PLAYER_ID, pid);

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
	public void createBoard(String title, Language language, int timeout, String createTab, String robotType, int opponentsCount, DataResponse<Id> response) {
		final Request request = new Request(REQUEST_TYPE_CREATE_GAME);
		request.put(CreateGameOperation.PARAM_TITLE, title);
		request.put(CreateGameOperation.PARAM_LANGUAGE, language.getCode());
		request.put(CreateGameOperation.PARAM_TIMEOUT, timeout);
		request.put(CreateGameOperation.PARAM_TAB, createTab);
		request.put(CreateGameOperation.PARAM_ROBOT_TYPE, robotType);
		request.put(CreateGameOperation.PARAM_OPPONENTS_COUNT, opponentsCount);

		execute(request, new TheRequestListener<>(response));
	}

	@Override
	public void openBoard(long boardId, DataResponse<ScribbleBoard> response) {
		final Request request = new Request(REQUEST_TYPE_OPEN_GAME);
		request.put(OpenBoardOperation.PARAM_BOARD_ID, boardId);
		execute(request, new TheRequestListener<>(response));
	}


	@Override
	public void passTurn(long boardId, DataResponse<ScribbleChanges> response) {
		final Request request = new Request(REQUEST_TYPE_BOARD_ACTION);
		request.put(BoardActionOperation.PARAM_BOARD_ID, boardId);
		request.put(BoardActionOperation.PARAM_ACTION_TYPE, BoardActionOperation.ACTION_TYPE_PASS);
		execute(request, new TheRequestListener<>(response));
	}

	@Override
	public void resignGame(long boardId, DataResponse<ScribbleChanges> response) {
		final Request request = new Request(REQUEST_TYPE_BOARD_ACTION);
		request.put(BoardActionOperation.PARAM_BOARD_ID, boardId);
		request.put(BoardActionOperation.PARAM_ACTION_TYPE, BoardActionOperation.ACTION_TYPE_RESIGN);
		execute(request, new TheRequestListener<>(response));
	}

	@Override
	public void makeTurn(long boardId, ScribbleWord word, DataResponse<ScribbleChanges> response) {
		final Request request = new Request(REQUEST_TYPE_BOARD_ACTION);
		request.put(BoardActionOperation.PARAM_WORD, word);
		request.put(BoardActionOperation.PARAM_BOARD_ID, boardId);
		request.put(BoardActionOperation.PARAM_ACTION_TYPE, BoardActionOperation.ACTION_TYPE_MAKE);
		execute(request, new TheRequestListener<>(response));
	}

	@Override
	public void exchangeTiles(long boardId, ScribbleTile[] tiles, DataResponse<ScribbleChanges> response) {
		final Request request = new Request(REQUEST_TYPE_BOARD_ACTION);
		request.put(BoardActionOperation.PARAM_TILES_COUNT, tiles.length);
		for (int i = 0; i < tiles.length; i++) {
			request.put(BoardActionOperation.PARAM_TILE_ITEM + "_" + i, tiles[i]);
		}

		request.put(BoardActionOperation.PARAM_BOARD_ID, boardId);
		request.put(BoardActionOperation.PARAM_ACTION_TYPE, BoardActionOperation.ACTION_TYPE_EXCHANGE);
		execute(request, new TheRequestListener<>(response));
	}

	@Override
	public void getWordEntry(String word, String lang, DataResponse<WordEntry> response) {
		final Request request = new Request(REQUEST_TYPE_DICT_WORD);
		request.put(LoadWordEntryOperation.PARAM_WORD, word);
		request.put(LoadWordEntryOperation.PARAM_LANGUAGE, lang);
		execute(request, new TheRequestListener<>(response));
	}

	@Override
	public void loadInfoPage(String name, DataResponse<InfoPage> response) {
		final Request request = new Request(REQUEST_TYPE_LOAD_INFO);
		request.put(LoadInfoPageOperation.PARAM_PAGE, name);
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