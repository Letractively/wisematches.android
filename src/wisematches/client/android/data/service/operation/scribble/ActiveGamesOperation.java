package wisematches.client.android.data.service.operation.scribble;

import com.foxykeep.datadroid.requestmanager.Request;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.scribble.ScribbleDescriptor;
import wisematches.client.android.data.service.operation.JSONOperation;
import wisematches.client.android.data.service.parser.scribble.ScribbleDescriptorParser;

import java.util.ArrayList;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ActiveGamesOperation extends JSONOperation.List<ScribbleDescriptor> {
	public static final String PLAYER_ID = "PLAYER_ID";

	public ActiveGamesOperation() {
	}

	@Override
	protected HttpRequest createRequest(Request request) {
		final HttpGet httpGet = new HttpGet("/playground/scribble/active.ajax");

		final BasicHttpParams params = new BasicHttpParams();
		params.setParameter("pid", request.getLong(PLAYER_ID));
		httpGet.setParams(params);

		return httpGet;
	}

	@Override
	protected ArrayList<ScribbleDescriptor> createResponse(JSONObject data) throws JSONException {
		final ArrayList<ScribbleDescriptor> res = new ArrayList<>();
		final JSONArray jsonGames = data.getJSONArray("games");
		for (int i = 0; i < jsonGames.length(); i++) {
			res.add(ScribbleDescriptorParser.parse(jsonGames.getJSONObject(i)));
		}

		final JSONArray jsonProposals = data.getJSONArray("proposals");
		// TODO: not implemented

		return res;
	}
}
