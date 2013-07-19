package wisematches.client.android.data.service.parser.scribble;

import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.Time;
import wisematches.client.android.data.model.scribble.ScribbleStatus;
import wisematches.client.android.data.service.parser.TimeParser;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleStatusParser {
	public static ScribbleStatus parse(JSONObject o) throws JSONException {
		final boolean active = o.getBoolean("active");
		final String resolution = o.getString("resolution");
		final Time spentTime = TimeParser.parse(o.optJSONObject("spentTime"));
		final Time startedTime = TimeParser.parse(o.optJSONObject("startedTime"));
		final Time finishedTime = TimeParser.parse(o.optJSONObject("finishedTime"));
		final Time remainedTime = TimeParser.parse(o.optJSONObject("remainedTime"));
		final long playerTurn = o.getLong("playerTurn");
		final long lastChange = o.getLong("lastChange");
		return new ScribbleStatus(startedTime, finishedTime, remainedTime, spentTime, active, resolution, playerTurn, lastChange);
	}
}
