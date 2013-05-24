package wisematches.client.android.data.service.operation.scribble;

import com.foxykeep.datadroid.exception.DataException;
import com.foxykeep.datadroid.requestmanager.Request;
import org.apache.http.HttpRequest;
import org.json.JSONException;
import wisematches.client.android.data.model.scribble.ScribbleChanges;
import wisematches.client.android.data.service.operation.JSONOperation;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardActionOperation extends JSONOperation.Primitive<ScribbleChanges> {
	public static final String PARAM_ACTION_TYPE = "PARAM_ACTION_TYPE";

	public static final String ACTION_TYPE_MAKE = "MAKE";
	public static final String ACTION_TYPE_PASS = "PASS";
	public static final String ACTION_TYPE_EXCHANGE = "EXCHANGE";

	@Override
	protected HttpRequest createRequest(Request request) throws DataException {
		return null;
	}

	@Override
	protected ScribbleChanges createResponse(Object data) throws JSONException {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}
}
