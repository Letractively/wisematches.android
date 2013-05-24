package wisematches.client.android.data.service.parser.scribble;

import org.json.JSONObject;
import wisematches.client.android.data.model.scribble.CriterionViolation;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class CriterionViolationParser {
	private CriterionViolationParser() {
	}

	public static CriterionViolation parse(JSONObject o) {
		return new CriterionViolation(o.optString("code"), o.optString("longDescription"), o.optString("shortDescription"));
	}
}
