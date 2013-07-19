package wisematches.client.android.data.service.parser.scribble;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.scribble.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class ScribbleBoardParser {
	public static ScribbleSnapshot parse(JSONObject data) throws JSONException {
		final ScribbleDescriptor descriptor = ScribbleDescriptorParser.parse(data);

		final int allHandBonus = data.getInt("allHandBonus");
		final JSONArray jsonBonuses = data.getJSONArray("bonuses");
		final ScoreBonus[] bonuses = new ScoreBonus[jsonBonuses.length()];
		for (int i = 0; i < bonuses.length; i++) {
			JSONObject o = jsonBonuses.getJSONObject(i);
			bonuses[i] = new ScoreBonus(o.getInt("row"), o.getInt("column"), ScoreBonus.Type.valueOf(o.getString("type")));
		}
		final ScoreEngine scoreEngine = new ScoreEngine(bonuses, allHandBonus);

		final JSONObject jsonBank = data.getJSONObject("bank");
		final JSONArray letterDescriptions = jsonBank.getJSONArray("letterDescriptions");
		final ScribbleLetter[] letters = new ScribbleLetter[letterDescriptions.length()];
		for (int i = 0; i < letterDescriptions.length(); i++) {
			final JSONObject jsonLetter = letterDescriptions.getJSONObject(i);
			letters[i] = new ScribbleLetter(jsonLetter.getString("letter").charAt(0), jsonLetter.getInt("count"), jsonLetter.getInt("cost"));
		}
		final ScribbleBank scribbleBank = new ScribbleBank(letters);

		final JSONArray jsonMoves = data.getJSONArray("moves");
		final List<ScribbleMove> moves = new ArrayList<>();
		for (int i = 0; i < jsonMoves.length(); i++) {
			moves.add(ScribbleMoveParser.parse(jsonMoves.getJSONObject(i)));
		}

		final JSONArray jsonHandTiles = data.optJSONArray("handTiles");
		final ScribbleTile[] handTiles = new ScribbleTile[7];
		if (jsonHandTiles != null) {
			for (int i = 0; i < jsonHandTiles.length(); i++) {
				handTiles[i] = ScribbleTileParser.parseTile(jsonHandTiles.getJSONObject(i));
			}
		}

		return new ScribbleSnapshot(descriptor, scoreEngine, scribbleBank, moves, handTiles);
	}
}
