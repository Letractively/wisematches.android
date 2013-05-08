package wisematches.client.android.data.service;

import android.content.Context;
import android.os.Bundle;
import com.foxykeep.datadroid.exception.ConnectionException;
import com.foxykeep.datadroid.exception.CustomRequestException;
import com.foxykeep.datadroid.exception.DataException;
import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.qwe.RequestType;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AuthenticationOperation extends AbstractOperation {
	protected AuthenticationOperation() {
		super(RequestType.AUTHENTICATION);
	}

	@Override
	public final Bundle execute(Context context, Request request) throws ConnectionException, DataException, CustomRequestException {
		final Personality execute = execute(context, request.getString("username"), request.getString("password"));

		final Bundle bundle = new Bundle();
		bundle.putParcelable("personality", execute);
		return bundle;
	}

	protected abstract Personality execute(Context context, String username, String password) throws ConnectionException, DataException, CustomRequestException;

	public static void auth(RequestManager requestManager, String username, String password) {
		final Request request = RequestType.AUTHENTICATION.create();
		request.put("username", username);
		request.put("password", password);

		requestManager.execute(request, new RequestManager.RequestListener() {
			@Override
			public void onRequestFinished(Request request, Bundle resultData) {
				Personality personality = resultData.getParcelable("personality");

			}

			@Override
			public void onRequestConnectionError(Request request, int statusCode) {
			}

			@Override
			public void onRequestDataError(Request request) {
			}

			@Override
			public void onRequestCustomError(Request request, Bundle resultData) {
			}
		});
	}
}
