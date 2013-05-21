package wisematches.client.android.data.service.parser.scribble;

import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.Language;
import wisematches.client.android.data.model.scribble.ScribbleSettings;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class ScribbleSettingsParser {
	private ScribbleSettingsParser() {
	}

	public static ScribbleSettings parse(JSONObject o) throws JSONException {
		return new ScribbleSettings(
				o.getString("title"),
				o.getInt("daysPerMove"),
				Language.valueOf(o.getString("language").toUpperCase()),
				o.optBoolean("scratch", false));
	}
}
