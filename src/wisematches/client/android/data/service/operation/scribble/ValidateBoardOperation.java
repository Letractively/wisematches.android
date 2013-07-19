package wisematches.client.android.data.service.operation.scribble;

import com.foxykeep.datadroid.exception.DataException;
import com.foxykeep.datadroid.requestmanager.Request;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.scribble.ScribbleChanges;
import wisematches.client.android.data.service.operation.JSONOperation;
import wisematches.client.android.data.service.parser.scribble.ScribbleChangesParser;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ValidateBoardOperation extends JSONOperation.Primitive<ScribbleChanges> {
	public static final String PARAM_BOARD_ID = "PARAM_BOARD_ID";
	public static final String PARAM_LAST_CHANGE = "PARAM_LAST_CHANGE";

	@Override
	protected HttpRequest createRequest(Request request) throws DataException {
		final long id = request.getLong(PARAM_BOARD_ID);
		final long lastChange = request.getLong(PARAM_LAST_CHANGE);
		return new HttpPost("/playground/scribble/observe.ajax?b=" + id + "&l=" + lastChange);
	}

	@Override
	protected ScribbleChanges createResponse(Object data) throws JSONException {
		return ScribbleChangesParser.parse((JSONObject) data);
	}
}
