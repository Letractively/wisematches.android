package wisematches.client.android.data.service.operation.person;

import android.util.Base64;
import com.foxykeep.datadroid.requestmanager.Request;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.service.operation.JSONOperation;
import wisematches.client.android.data.service.parser.person.PersonalityParser;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SignInPlayerOperation extends JSONOperation.Primitive<Personality> {
	public static final String PARAM_VISITOR = "visitor";

	public static final String PARAM_USERNAME = "username";
	public static final String PARAM_PASSWORD = "password";

	public SignInPlayerOperation() {
	}

	@Override
	protected HttpRequest createRequest(Request request) {
		HttpPost httpPost;

		if (request.getBoolean(PARAM_VISITOR)) {
			httpPost = new HttpPost("/account/loginGuest?continue=/account/login.ajax");
		} else {
			httpPost = new HttpPost("/account/login.ajax");
			httpPost.setHeader("Authorization", "Basic " + Base64.encodeToString((request.getString(PARAM_USERNAME) + ":" + request.getString(PARAM_PASSWORD)).getBytes(), Base64.NO_WRAP));
		}
		return httpPost;
	}

	@Override
	protected Personality createResponse(Object data) throws JSONException {
		return PersonalityParser.parse((JSONObject) data);
	}
}
