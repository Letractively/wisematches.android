package wisematches.client.android.data.service.operation.scribble;

import com.foxykeep.datadroid.requestmanager.Request;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.scribble.CriterionViolation;
import wisematches.client.android.data.model.scribble.ScribbleProposal;
import wisematches.client.android.data.model.scribble.WaitingGames;
import wisematches.client.android.data.service.operation.JSONOperation;
import wisematches.client.android.data.service.parser.proposal.CriterionViolationParser;
import wisematches.client.android.data.service.parser.proposal.ScribbleProposalParser;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WaitingGamesOperation extends JSONOperation.Primitive<WaitingGames> {
	private static final CriterionViolation[] NO_CRITERION_VIOLATIONS = new CriterionViolation[0];

	@Override
	protected HttpRequest createRequest(Request request) {
		return new HttpGet("/playground/scribble/join.ajax");
	}

	@Override
	protected WaitingGames createResponse(Object data) throws JSONException {
		final JSONObject obj = (JSONObject) data;
		final JSONArray jsonGames = obj.optJSONArray("proposalViews");
		final ScribbleProposal[] proposals = new ScribbleProposal[jsonGames.length()];
		for (int i = 0; i < jsonGames.length(); i++) {
			proposals[i] = ScribbleProposalParser.parse(jsonGames.getJSONObject(i));
		}

		CriterionViolation[] violations = NO_CRITERION_VIOLATIONS;
		final JSONArray jsonViolations = obj.optJSONArray("globalViolations");
		if (jsonViolations != null) {
			violations = new CriterionViolation[jsonViolations.length()];
			for (int i = 0; i < jsonViolations.length(); i++) {
				violations[i] = CriterionViolationParser.parse(jsonViolations.getJSONObject(i));
			}
		}
		return new WaitingGames(proposals, violations);
	}
}
