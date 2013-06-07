package wisematches.client.android.data.service.operation.info;

import com.foxykeep.datadroid.exception.DataException;
import com.foxykeep.datadroid.requestmanager.Request;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import wisematches.client.android.data.model.info.InfoPage;
import wisematches.client.android.data.service.RequestRejectedException;
import wisematches.client.android.data.service.operation.JSONOperation;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class LoadInfoPageOperation extends JSONOperation.Primitive<InfoPage> {
	public static final String PARAM_PAGE = "PARAM_PAGE";

	@Override
	protected HttpRequest createRequest(Request request) throws DataException {
		return new HttpGet("/info/" + request.getString(PARAM_PAGE) + "?plain=true");
	}

	@Override
	protected Object parseResponseData(String content) throws JSONException, RequestRejectedException {
		return content;
	}

	@Override
	protected InfoPage createResponse(Object data) throws JSONException {
		return new InfoPage((String) data);
	}
}
