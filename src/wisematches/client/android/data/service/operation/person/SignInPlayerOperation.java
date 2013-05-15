package wisematches.client.android.data.service.operation.person;

import android.util.Base64;
import com.foxykeep.datadroid.requestmanager.Request;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.service.operation.JSONOperation;
import wisematches.client.android.data.service.parser.PersonalityParser;

import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SignInPlayerOperation extends JSONOperation.Primitive<Personality> {
	public static final String PARAM_USERNAME = "username";
	public static final String PARAM_PASSWORD = "password";

	private static final String ACCOUNT_LOGIN_URL = "/account/login.ajax";

	public SignInPlayerOperation() {
	}

	@Override
	protected HttpRequest createRequest(Request request) {
		final HttpPost httpPost = new HttpPost(ACCOUNT_LOGIN_URL);
		final String credentials = Base64.encodeToString((request.getString(PARAM_USERNAME) + ":" + request.getString(PARAM_PASSWORD)).getBytes(), Base64.NO_WRAP);
		httpPost.setHeader("Authorization", "Basic " + credentials);
		return httpPost;
	}

	@Override
	protected Personality createResponse(JSONObject data) throws JSONException {
		return PersonalityParser.parse(data);
	}
}
