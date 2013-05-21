package wisematches.client.android.data.service.parser.person;

import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.person.Personality;

import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PersonalityParser {
	public static Personality parse(JSONObject data) throws JSONException {
		String timeZone = data.optString("timeZone");
		return new Personality(
				data.getLong("id"),
				data.optString("nickname"),
				data.optString("language"),
				timeZone != null && !timeZone.isEmpty() ? TimeZone.getTimeZone(timeZone) : TimeZone.getDefault(),
				data.getString("type"),
				data.optString("membership", null),
				data.optBoolean("online", false));
	}
}
