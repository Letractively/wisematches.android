package wisematches.client.android.data.service;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import com.foxykeep.datadroid.exception.ConnectionException;
import com.foxykeep.datadroid.exception.CustomRequestException;
import com.foxykeep.datadroid.exception.DataException;
import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.service.RequestService;
import wisematches.client.android.WiseMatchesApplication;
import wisematches.client.android.data.service.operation.JSONOperation;
import wisematches.client.android.data.service.operation.dict.LoadWordEntryOperation;
import wisematches.client.android.data.service.operation.person.RegisterPlayerOperation;
import wisematches.client.android.data.service.operation.person.SignInPlayerOperation;
import wisematches.client.android.data.service.operation.scribble.*;
import wisematches.client.android.http.JSONConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class JSONRequestService extends RequestService {
	private JSONConnection jsonConnection;

	private final Map<Integer, JSONResponseOperation> operationMap = new HashMap<>();

	public JSONRequestService() {
		operationMap.put(JSONRequestManager.REQUEST_TYPE_AUTH, new JSONResponseOperation(new SignInPlayerOperation()));
		operationMap.put(JSONRequestManager.REQUEST_TYPE_REGISTER, new JSONResponseOperation(new RegisterPlayerOperation()));

		operationMap.put(JSONRequestManager.REQUEST_TYPE_OPEN_GAME, new JSONResponseOperation(new OpenBoardOperation()));
		operationMap.put(JSONRequestManager.REQUEST_TYPE_CREATE_GAME, new JSONResponseOperation(new CreateGameOperation()));
		operationMap.put(JSONRequestManager.REQUEST_TYPE_WAITING_GAMES, new JSONResponseOperation(new WaitingGamesOperation()));
		operationMap.put(JSONRequestManager.REQUEST_TYPE_ACTIVE_GAMES, new JSONResponseOperation(new ActiveGamesOperation()));

		operationMap.put(JSONRequestManager.REQUEST_TYPE_PROCESS_PROPOSAL, new JSONResponseOperation(new ProcessProposalOperation()));

		operationMap.put(JSONRequestManager.REQUEST_TYPE_BOARD_ACTION, new JSONResponseOperation(new BoardActionOperation()));

		operationMap.put(JSONRequestManager.REQUEST_TYPE_DICT_WORD, new JSONResponseOperation(new LoadWordEntryOperation()));
	}

	@Override
	public void onCreate() {
		super.onCreate();

		jsonConnection = new JSONConnection(((WiseMatchesApplication) getApplication()).getSecurityContext());
	}

	@Override
	public void onDestroy() {
		jsonConnection.release();

		super.onDestroy();
	}

	@Override
	public Operation getOperationForType(int requestType) {
		return operationMap.get(requestType);
	}

	private class JSONResponseOperation implements Operation {
		private final JSONOperation<?> jsonOperation;

		private JSONResponseOperation(JSONOperation<?> jsonOperation) {
			this.jsonOperation = jsonOperation;
		}

		@Override
		@SuppressWarnings("unchecked")
		public Bundle execute(Context context, Request request) throws ConnectionException, DataException, CustomRequestException {
			Object res = jsonOperation.execute(request, jsonConnection);
			if (res == null) {
				return null;
			}

			if (jsonOperation instanceof JSONOperation.Primitive) {
				final Bundle b = new Bundle(JSONRequestService.class.getClassLoader());
				b.putString(JSONRequestManager.BUNDLE_EXTRA_RESPONSE_TYPE, JSONRequestManager.BUNDLE_EXTRA_RESPONSE_TYPE_PRIMITIVE);
				b.putParcelable(JSONRequestManager.BUNDLE_EXTRA_RESPONSE_TYPE_PRIMITIVE, (Parcelable) res);
				return b;
			} else if (jsonOperation instanceof JSONOperation.List) {
				final Bundle b = new Bundle(JSONRequestService.class.getClassLoader());
				b.putString(JSONRequestManager.BUNDLE_EXTRA_RESPONSE_TYPE, JSONRequestManager.BUNDLE_EXTRA_RESPONSE_TYPE_LIST);
				b.putParcelableArrayList(JSONRequestManager.BUNDLE_EXTRA_RESPONSE_TYPE_LIST, (ArrayList<? extends Parcelable>) res);
				return b;
			} else {
				throw new DataException("Incorrect result of operation: " + jsonOperation.getClass() + " returned type " + res.getClass());
			}
		}
	}

	protected Bundle onCustomRequestException(Request request, CustomRequestException exception) {
		if (exception instanceof RequestRejectedException) {
			final RequestRejectedException err = (RequestRejectedException) exception;
			final Bundle bundle = new Bundle();
			bundle.putString("code", err.getCode());
			bundle.putString("message", err.getMessage());
			return bundle;
		}
		return null;
	}
}