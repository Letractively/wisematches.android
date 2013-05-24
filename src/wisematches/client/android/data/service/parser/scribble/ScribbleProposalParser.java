package wisematches.client.android.data.service.parser.scribble;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.Time;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.model.scribble.CriterionViolation;
import wisematches.client.android.data.model.scribble.ScribbleProposal;
import wisematches.client.android.data.model.scribble.ScribbleSettings;
import wisematches.client.android.data.service.parser.TimeParser;
import wisematches.client.android.data.service.parser.person.PersonalityParser;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class ScribbleProposalParser {
	private static final CriterionViolation[] NO_CRITERION_VIOLATIONS = new CriterionViolation[0];

	private ScribbleProposalParser() {
	}

	public static ScribbleProposal parse(JSONObject o) throws JSONException {
		final long id = o.getLong("id");
		final ScribbleSettings settings = ScribbleSettingsParser.parse(o.getJSONObject("settings"));

		Time creationDate = TimeParser.parse(o.getJSONObject("creationDate"));
		long initiator = o.getLong("initiator");

		final JSONArray jsonPlayers = o.getJSONArray("players");
		Personality[] players = new Personality[jsonPlayers.length()];
		for (int i = 0; i < jsonPlayers.length(); i++) {
			players[i] = PersonalityParser.parse(jsonPlayers.optJSONObject(i));
		}

		final JSONArray jsonJoined = o.getJSONArray("joinedPlayers");
		long[] joinedPlayers = new long[jsonJoined.length()];
		for (int i = 0; i < jsonJoined.length(); i++) {
			joinedPlayers[i] = jsonJoined.getLong(i);
		}

		boolean ready = o.getBoolean("ready");
		String proposalType = o.getString("proposalType");

		CriterionViolation[] violations = NO_CRITERION_VIOLATIONS;
		final JSONArray jsonViolations = o.optJSONArray("violations");
		if (jsonViolations != null) {
			violations = new CriterionViolation[jsonViolations.length()];
			for (int i = 0; i < jsonViolations.length(); i++) {
				violations[i] = CriterionViolationParser.parse(jsonViolations.getJSONObject(i));
			}
		}
		return new ScribbleProposal(id, settings, creationDate, initiator, players, joinedPlayers, ready, proposalType, violations);
	}
}
