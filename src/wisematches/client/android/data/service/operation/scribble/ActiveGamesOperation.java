package wisematches.client.android.data.service.operation.scribble;

import com.foxykeep.datadroid.requestmanager.Request;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONObject;
import wisematches.client.android.data.model.scribble.ScribbleDescriptor;
import wisematches.client.android.data.service.operation.JSONOperation;

import java.util.ArrayList;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ActiveGamesOperation extends JSONOperation.List<ScribbleDescriptor> {
	public ActiveGamesOperation() {
	}

	@Override
	protected HttpRequest createRequest(Request request) {
		final HttpGet httpGet = new HttpGet("/playground/scribble/active.ajax");

		final BasicHttpParams params = new BasicHttpParams();
		params.setParameter("pid", request.getLong("pid"));
		httpGet.setParams(params);

		return httpGet;
	}

	@Override
	protected ArrayList<ScribbleDescriptor> createResponse(JSONObject data) {
		return new ArrayList<>();
	}
}
