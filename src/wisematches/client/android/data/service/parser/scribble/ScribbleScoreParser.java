package wisematches.client.android.data.service.parser.scribble;

import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.scribble.ScribbleScore;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleScoreParser {
	public static ScribbleScore parse(JSONObject data) throws JSONException {
		int points = data.getInt("points");
		int oldRating = data.getInt("oldRating");
		int newRating = data.getInt("newRating");
		boolean winner = data.getBoolean("winner");
		return new ScribbleScore(points, oldRating, newRating, winner);
	}
}
