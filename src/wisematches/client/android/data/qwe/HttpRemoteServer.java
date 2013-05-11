package wisematches.client.android.data.qwe;

import android.content.Context;
import android.os.Bundle;
import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager;
import wisematches.client.android.data.RemoteServer;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.model.scribble.ScribbleDescriptor;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HttpRemoteServer implements RemoteServer {
	private final Context context;
	private final RequestManager requestManager;

	public HttpRemoteServer(Context context, RequestManager requestManager) {
		this.context = context;
		this.requestManager = requestManager;
	}

	@Override
	public void authenticate(String username, String password, RemoteResponse<Personality> response) {
		final Request request = RequestType.AUTHENTICATION.create();
		request.put("username", username);
		request.put("password", password);

/*
		requestManager.execute(request, new TheRequestListener<?>() {
			@Override
			public void onRequestFinished(Request request, Bundle resultData) {
			}
		});
*/
	}

    @Override
    public void getPersonality(long pid, RemoteResponse<Personality> response) {
        throw new UnsupportedOperationException("TODO: not implemented");
    }

    @Override
    public void getActiveGames(long pid, RemoteResponse<ScribbleDescriptor> response) {
        throw new UnsupportedOperationException("TODO: not implemented");
    }

	private abstract class TheRequestListener implements RequestManager.RequestListener {
		private final RemoteResponse<?> response;

		protected TheRequestListener(RemoteResponse<?> response) {
			this.response = response;
		}

		@Override
		public void onRequestFinished(Request request, Bundle resultData) {
			if (resultData.getBoolean("success", false)) {
//				response.onSuccess((Object)resultData.get("data"));
			} else {
				response.onFailure(resultData.getString("code"), resultData.getString("message"));
			}
		}

		@Override
		public void onRequestConnectionError(Request request, int statusCode) {
			// TODO: common processing
		}

		@Override
		public void onRequestDataError(Request request) {
			// TODO: common processing
		}

		@Override
		public void onRequestCustomError(Request request, Bundle resultData) {
			// TODO: common processing
		}
	}
}
