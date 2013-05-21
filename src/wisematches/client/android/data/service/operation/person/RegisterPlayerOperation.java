package wisematches.client.android.data.service.operation.person;

import com.foxykeep.datadroid.exception.DataException;
import com.foxykeep.datadroid.requestmanager.Request;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.service.operation.JSONOperation;
import wisematches.client.android.data.service.parser.person.PersonalityParser;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RegisterPlayerOperation extends JSONOperation.Primitive<Personality> {
	public static final String PARAM_USERNAME = "username";
	public static final String PARAM_EMAIL = "email";
	public static final String PARAM_PASSWORD = "password";
	public static final String PARAM_CONFIRM = "confirm";
	public static final String PARAM_LANGUAGE = "language";
	public static final String PARAM_TIMEZONE = "timezone";

	@Override
	protected HttpRequest createRequest(Request request) throws DataException {
		final HttpPost httpPost = new HttpPost("/account/create.ajax");

		try {
			final JSONObject form = new JSONObject();
			form.put("nickname", request.getString(PARAM_USERNAME));
			form.put("email", request.getString(PARAM_EMAIL));
			form.put("password", request.getString(PARAM_PASSWORD));
			form.put("confirm", request.getString(PARAM_CONFIRM));
			form.put("language", request.getString(PARAM_LANGUAGE));
			form.put("timezone", request.getString(PARAM_TIMEZONE));

			httpPost.setEntity(new StringEntity(form.toString(), HTTP.UTF_8));
		} catch (Exception ex) {
			throw new DataException("Data preparation error", ex);
		}
		return httpPost;
	}

	@Override
	protected Personality createResponse(JSONObject data) throws JSONException {
		return PersonalityParser.parse(data);
	}
}
