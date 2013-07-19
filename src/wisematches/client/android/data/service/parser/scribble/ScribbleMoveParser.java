package wisematches.client.android.data.service.parser.scribble;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.scribble.*;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleMoveParser {
	public static ScribbleMove parse(JSONObject data) throws JSONException {
		final int number = data.getInt("number");
		final int points = data.getInt("points");
		final long pid = data.getLong("player");
		final Date time = new Date(data.getJSONObject("time").getLong("millis"));

		final MoveType moveType = MoveType.valueOf(data.getString("type"));

		ScribbleMove move = null;
		switch (moveType) {
			case MAKE:
				final JSONObject jsonWord = data.getJSONObject("word");

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
		return move;
	}
}
