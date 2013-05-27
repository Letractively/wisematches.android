package wisematches.client.android.data.service.parser.scribble;

import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.scribble.ScribbleTile;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleTileParser {
	public static ScribbleTile parseTile(JSONObject data) throws JSONException {
		return new ScribbleTile(data.getInt("cost"), data.getInt("number"), data.getString("letter"));
	}
}
