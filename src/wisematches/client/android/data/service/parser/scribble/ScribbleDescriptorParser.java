package wisematches.client.android.data.service.parser.scribble;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.model.scribble.*;
import wisematches.client.android.data.service.parser.person.PersonalityParser;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleDescriptorParser {
	public static ScribbleDescriptor parse(JSONObject o) throws JSONException {
		final long id = o.getLong("id");
		final ScribbleStatus status = ScribbleStatusParser.parse(o);
		final ScribbleSettings settings = ScribbleSettingsParser.parse(o.getJSONObject("settings"));
		JSONArray jsonPlayers = o.getJSONArray("players");
		ScribbleHand[] hands = new ScribbleHand[jsonPlayers.length()];
		for (int j = 0; j < jsonPlayers.length(); j++) {
			final JSONObject po = jsonPlayers.getJSONObject(j);

			final Personality parse = PersonalityParser.parse(po.getJSONObject("info"));
			final ScribbleScore score = ScribbleScoreParser.parse(po.getJSONObject("score"));
			hands[j] = new ScribbleHand(parse, score);
		}
		return new ScribbleDescriptor(id, settings, hands, status);
	}
}
