package wisematches.client.android.data.service.operation.scribble;

import com.foxykeep.datadroid.requestmanager.Request;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.scribble.ActiveGames;
import wisematches.client.android.data.model.scribble.ScribbleDescriptor;
import wisematches.client.android.data.model.scribble.ScribbleProposal;
import wisematches.client.android.data.service.operation.JSONOperation;
import wisematches.client.android.data.service.parser.proposal.ScribbleProposalParser;
import wisematches.client.android.data.service.parser.scribble.ScribbleDescriptorParser;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ActiveGamesOperation extends JSONOperation.Primitive<ActiveGames> {
	public static final String PLAYER_ID = "PLAYER_ID";

	private static final ScribbleProposal[] EMPTY_PROPOSALS = new ScribbleProposal[0];
	private static final ScribbleDescriptor[] EMPTY_DESCRIPTORS = new ScribbleDescriptor[0];

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
	protected ActiveGames createResponse(Object data) throws JSONException {
		final JSONObject obj = (JSONObject) data;

		ScribbleDescriptor[] descriptors = EMPTY_DESCRIPTORS;
		final JSONArray jsonGames = obj.optJSONArray("games");
		if (jsonGames != null) {
			descriptors = new ScribbleDescriptor[jsonGames.length()];
			for (int i = 0; i < jsonGames.length(); i++) {
				descriptors[i] = ScribbleDescriptorParser.parse(jsonGames.getJSONObject(i));
			}
		}

		ScribbleProposal[] proposals = EMPTY_PROPOSALS;
		final JSONArray jsonProposals = obj.optJSONArray("proposals");
		if (jsonProposals != null) {
			proposals = new ScribbleProposal[jsonProposals.length()];
			for (int i = 0; i < jsonProposals.length(); i++) {
				proposals[i] = ScribbleProposalParser.parse(jsonProposals.getJSONObject(i));
			}
		}
		return new ActiveGames(proposals, descriptors);
	}
}
