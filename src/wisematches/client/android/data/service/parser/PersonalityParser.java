package wisematches.client.android.data.service.parser;

import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.person.Personality;

import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PersonalityParser {
	public static Personality parse(JSONObject data) throws JSONException {
		return new Personality(
				data.getLong("id"),
				data.getString("nickname"),
				data.getString("language"),
				TimeZone.getTimeZone(data.getString("timeZone")),
				data.getString("type"),
				data.optString("membership", null),
				true);
	}
}
