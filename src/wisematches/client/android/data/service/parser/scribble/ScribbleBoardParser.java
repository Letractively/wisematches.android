package wisematches.client.android.data.service.parser.scribble;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.scribble.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class ScribbleBoardParser {
	public static ScribbleBoard parseGame(JSONObject data) throws JSONException {
		final ScribbleDescriptor descriptor = ScribbleDescriptorParser.parse(data);

		final int allHandBonus = data.getInt("allHandBonus");
		final JSONArray jsonBonuses = data.getJSONArray("bonuses");
		final ScoreBonus[] bonuses = new ScoreBonus[jsonBonuses.length()];
		for (int i = 0; i < bonuses.length; i++) {
			JSONObject o = jsonBonuses.getJSONObject(i);
			bonuses[i] = new ScoreBonus(o.getInt("row"), o.getInt("column"), ScoreBonus.Type.valueOf(o.getString("type")));
		}
		final ScoreEngine scoreEngine = new ScoreEngine(bonuses, allHandBonus);

		final JSONArray jsonMoves = data.getJSONArray("moves");
		List<ScribbleMove> moves = new ArrayList<>();
		for (int i = 0; i < jsonMoves.length(); i++) {
			final JSONObject jsonMove = jsonMoves.getJSONObject(i);
			final int number = jsonMove.getInt("number");
			final int points = jsonMove.getInt("points");
			final long pid = jsonMove.getLong("player");
			final Date time = new Date(jsonMove.getJSONObject("time").getLong("millis"));

			final MoveType moveType = MoveType.valueOf(jsonMove.getString("type"));

			ScribbleMove move = null;
			switch (moveType) {
				case MAKE:
					final JSONObject jsonWord = jsonMove.getJSONObject("word");

					final JSONObject jsonPosition = jsonWord.getJSONObject("position");

					final JSONArray jsonTiles = jsonWord.getJSONArray("tiles");
					final ScribbleTile[] tiles = new ScribbleTile[jsonTiles.length()];
					for (int j = 0; j < jsonTiles.length(); j++) {
						tiles[j] = ScribbleTileParser.parseTile(jsonTiles.getJSONObject(j));
					}
					final ScribbleWord w = new ScribbleWord(
							jsonPosition.getInt("row"),
							jsonPosition.getInt("column"),
							WordDirection.valueOf(jsonWord.getString("direction")),
							tiles);
					move = new ScribbleMove.Make(number, points, time, pid, w);
					break;
				case PASS:
					move = new ScribbleMove.Pass(number, points, time, pid);
					break;
				case EXCHANGE:
					move = new ScribbleMove.Exchange(number, points, time, pid);
					break;
			}
			moves.add(move);
		}


		final JSONObject jsonBank = data.getJSONObject("bank");
		final JSONArray letterDescriptions = jsonBank.getJSONArray("letterDescriptions");
		final ScribbleLetter[] letters = new ScribbleLetter[letterDescriptions.length()];
		for (int i = 0; i < letterDescriptions.length(); i++) {
			final JSONObject jsonLetter = letterDescriptions.getJSONObject(i);
			letters[i] = new ScribbleLetter(jsonLetter.getString("letter").charAt(0), jsonLetter.getInt("count"), jsonLetter.getInt("cost"));
		}
		final ScribbleBank scribbleBank = new ScribbleBank(letters);

		JSONArray jsonHandTiles = data.optJSONArray("handTiles");
		ScribbleTile[] handTiles = new ScribbleTile[7];
		if (jsonHandTiles != null) {
			for (int i = 0; i < jsonHandTiles.length(); i++) {
				handTiles[i] = ScribbleTileParser.parseTile(jsonHandTiles.getJSONObject(i));
			}
		}

		return new ScribbleBoard(descriptor, scoreEngine, scribbleBank, moves, handTiles);
	}
}
