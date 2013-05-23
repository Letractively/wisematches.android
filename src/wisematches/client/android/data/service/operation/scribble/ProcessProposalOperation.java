package wisematches.client.android.data.service.operation.scribble;

import com.foxykeep.datadroid.exception.DataException;
import com.foxykeep.datadroid.requestmanager.Request;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import wisematches.client.android.data.model.Id;
import wisematches.client.android.data.service.operation.JSONOperation;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ProcessProposalOperation extends JSONOperation.Primitive<Id> {
	public static final String PARAM_ID = "PARAM_ID";
	public static final String PARAM_TYPE = "PARAM_TYPE";

	@Override
	protected HttpRequest createRequest(Request request) throws DataException {
		final long id = request.getLong(PARAM_ID);
		final boolean accept = request.getBoolean(PARAM_TYPE);
		return new HttpPost("/playground/scribble/" + (accept ? "accept" : "decline") + ".ajax?p=" + id);
	}

	@Override
	protected Id createResponse(Object data) throws JSONException {
		if (data != null) {
			return new Id(((Number) data).longValue());
		}
		return null;
	}
}
