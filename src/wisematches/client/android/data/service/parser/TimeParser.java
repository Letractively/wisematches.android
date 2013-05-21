package wisematches.client.android.data.service.parser;

import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.Time;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class TimeParser {
	private TimeParser() {
	}

	public static Time parse(JSONObject o) throws JSONException {
		if (o == null) {
			return null;
		}
		return new Time(o.getLong("millis"), o.getString("text"));
	}
}

