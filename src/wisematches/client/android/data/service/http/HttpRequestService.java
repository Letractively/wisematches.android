package wisematches.client.android.data.service.http;

import wisematches.client.android.data.service.AbstractRequestService;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HttpRequestService extends AbstractRequestService {
//	private final WiseMatchesWebServer webServer = new WiseMatchesWebServer();

	public HttpRequestService() {
//		super(new HttpAuthenticationOperation());
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public Operation getOperationForType(int requestType) {
		final Operation operationForType = super.getOperationForType(requestType);

		return operationForType;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

//		webServer.release();
	}
}
