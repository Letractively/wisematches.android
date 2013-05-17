package wisematches.client.android.data.service;

import com.foxykeep.datadroid.requestmanager.Request;
import wisematches.client.android.data.service.operation.person.RegisterPlayerOperation;
import wisematches.client.android.data.service.operation.person.SignInPlayerOperation;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class JSONRequestFactory {
	public static final int REQUEST_TYPE_AUTH = 1;
	public static final int REQUEST_TYPE_REGISTER = 2;

	public static final String BUNDLE_EXTRA_RESPONSE_TYPE = "wisematches.client.extra.response.type";
	public static final String BUNDLE_EXTRA_RESPONSE_TYPE_LIST = "wisematches.client.extra.response.type.list";
	public static final String BUNDLE_EXTRA_RESPONSE_TYPE_PRIMITIVE = "wisematches.client.extra.response.type.primitive";

	private JSONRequestFactory() {
	}

	public static Request getAuthRequest(String username, String password) {
		final Request request = new Request(REQUEST_TYPE_AUTH);
		request.setMemoryCacheEnabled(false);
		if (username == null && password == null) {
			request.put(SignInPlayerOperation.PARAM_VISITOR, true);
		} else {
			request.put(SignInPlayerOperation.PARAM_USERNAME, username);
			request.put(SignInPlayerOperation.PARAM_PASSWORD, password);
		}
		return request;
	}

	public static Request getRegisterRequest(String username, String email, String password, String confirm, String language, String timezone) {
		final Request request = new Request(REQUEST_TYPE_REGISTER);
		request.setMemoryCacheEnabled(false);
		request.put(RegisterPlayerOperation.PARAM_USERNAME, username);
		request.put(RegisterPlayerOperation.PARAM_EMAIL, email);
		request.put(RegisterPlayerOperation.PARAM_PASSWORD, password);
		request.put(RegisterPlayerOperation.PARAM_CONFIRM, confirm);
		request.put(RegisterPlayerOperation.PARAM_LANGUAGE, language);
		request.put(RegisterPlayerOperation.PARAM_TIMEZONE, timezone);
		return request;
	}
}
