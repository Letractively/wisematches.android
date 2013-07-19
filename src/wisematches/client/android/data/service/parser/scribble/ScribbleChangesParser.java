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
public class ScribbleChangesParser {
	public static ScribbleChanges parse(JSONObject data) throws JSONException {
		final JSONArray jsonScores = data.getJSONArray("scores");
		final ScribbleScore[] scores = new ScribbleScore[jsonScores.length()];
		for (int i = 0; i < jsonScores.length(); i++) {
			scores[i] = ScribbleScoreParser.parse(jsonScores.getJSONObject(i));
		}

		final ScribbleStatus status = ScribbleStatusParser.parse(data.getJSONObject("status"));

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
		return new ScribbleChanges(status, scores, moves, handTiles);
	}
}
