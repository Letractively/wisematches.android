package wisematches.client.android.data.service.operation.scribble;

import com.foxykeep.datadroid.exception.DataException;
import com.foxykeep.datadroid.requestmanager.Request;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.Id;
import wisematches.client.android.data.service.operation.JSONOperation;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class CreateGameOperation extends JSONOperation.Primitive<Id> {
	public static final String PARAM_TITLE = "PARAM_TITLE";
	public static final String PARAM_TIMEOUT = "PARAM_TIMEOUT";
	public static final String PARAM_LANGUAGE = "PARAM_LANGUAGE";
	public static final String PARAM_TAB = "PARAM_TAB";
	public static final String PARAM_ROBOT_TYPE = "PARAM_ROBOT_TYPE";
	public static final String PARAM_OPPONENTS_COUNT = "PARAM_OPPONENTS_COUNT";

	@Override
	protected HttpRequest createRequest(Request request) throws DataException {
		final HttpPost httpPost = new HttpPost("/playground/scribble/create.ajax");

		try {
			final JSONObject form = new JSONObject();
			form.put("title", request.getString(PARAM_TITLE));
			form.put("daysPerMove", request.getInt(PARAM_TIMEOUT));
			form.put("boardLanguage", request.getString(PARAM_LANGUAGE));
			form.put("createTab", request.getString(PARAM_TAB));
			form.put("robotType", request.getString(PARAM_ROBOT_TYPE));
			form.put("opponentsCount", request.getInt(PARAM_OPPONENTS_COUNT));

			httpPost.setEntity(new StringEntity(form.toString(), HTTP.UTF_8));
		} catch (Exception ex) {
			throw new DataException("Data preparation error", ex);
		}
		return httpPost;
	}

	@Override
	protected Id createResponse(JSONObject data) throws JSONException {
		return new Id(data.getLong("board"));
	}
}
