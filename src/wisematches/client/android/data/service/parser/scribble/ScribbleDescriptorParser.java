package wisematches.client.android.data.service.parser.scribble;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.Time;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.model.scribble.ScribbleDescriptor;
import wisematches.client.android.data.model.scribble.ScribbleHand;
import wisematches.client.android.data.model.scribble.ScribbleSettings;
import wisematches.client.android.data.service.parser.TimeParser;
import wisematches.client.android.data.service.parser.person.PersonalityParser;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleDescriptorParser {
	public static ScribbleDescriptor parse(JSONObject o) throws JSONException {
		final long id = o.getLong("id");
		final ScribbleSettings settings = ScribbleSettingsParser.parse(o.getJSONObject("settings"));
		boolean active = o.getBoolean("active");
		String resolution = o.getString("resolution");
		Time spentTime = TimeParser.parse(o.optJSONObject("spentTime"));
		Time startedTime = TimeParser.parse(o.optJSONObject("startedTime"));
		Time finishedTime = TimeParser.parse(o.optJSONObject("finishedTime"));
		Time remainedTime = TimeParser.parse(o.optJSONObject("remainedTime"));
		long lastChange = o.getLong("lastChange");

		int playerTurnIndex = -1;
		long playerTurn = o.getLong("playerTurn");
		JSONArray jsonPlayers = o.getJSONArray("players");
		ScribbleHand[] players = new ScribbleHand[jsonPlayers.length()];
		for (int j = 0; j < jsonPlayers.length(); j++) {
			final JSONObject po = jsonPlayers.getJSONObject(j);
			final Personality parse = PersonalityParser.parse(po.getJSONObject("info"));
			if (parse != null && parse.getId() == playerTurn) {
				playerTurnIndex = j;
			}

			final JSONObject score = po.getJSONObject("score");
			players[j] = new ScribbleHand(parse,
					score.getInt("points"),
					score.getInt("oldRating"),
					score.getInt("newRating"),
					score.getBoolean("winner"));
		}
		return new ScribbleDescriptor(id, settings, players, active, resolution, playerTurnIndex, spentTime, startedTime, finishedTime, remainedTime, lastChange);
	}
}
