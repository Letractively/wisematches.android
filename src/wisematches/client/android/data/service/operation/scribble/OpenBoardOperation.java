package wisematches.client.android.data.service.operation.scribble;

import com.foxykeep.datadroid.exception.DataException;
import com.foxykeep.datadroid.requestmanager.Request;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.scribble.ScribbleSnapshot;
import wisematches.client.android.data.service.operation.JSONOperation;
import wisematches.client.android.data.service.parser.scribble.ScribbleBoardParser;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class OpenBoardOperation extends JSONOperation.Primitive<ScribbleSnapshot> {
	public static final String PARAM_BOARD_ID = "PARAM_BOARD_ID";

	@Override
	protected HttpRequest createRequest(Request request) throws DataException {
		final long id = request.getLong(PARAM_BOARD_ID);
		return new HttpPost("/playground/scribble/board/load.ajax?b=" + id);
	}

	@Override
	protected ScribbleSnapshot createResponse(Object data) throws JSONException {
		return ScribbleBoardParser.parseGame((JSONObject) data);
	}
}
