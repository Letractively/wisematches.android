package wisematches.client.android.data.service.operation.dict;

import com.foxykeep.datadroid.exception.DataException;
import com.foxykeep.datadroid.requestmanager.Request;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.scribble.WordEntry;
import wisematches.client.android.data.service.operation.JSONOperation;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class LoadWordEntryOperation extends JSONOperation.Primitive<WordEntry> {
	public static final String PARAM_WORD = "PARAM_WORD";
	public static final String PARAM_LANGUAGE = "PARAM_LANGUAGE";

	private static final String[] NO_ATTRS_VALUE = new String[0];

	@Override
	protected HttpRequest createRequest(Request request) throws DataException {
		final String word = request.getString(PARAM_WORD);
		final String lang = request.getString(PARAM_LANGUAGE);
		return new HttpGet("/playground/dictionary/loadWordEntry.ajax?l=" + lang + "&w=" + word);
	}

	@Override
	protected WordEntry createResponse(Object data) throws JSONException {
		final JSONObject o = (JSONObject) data;

		final String word = o.getString("word");
		final String definition = o.getString("definition");

		final String[] attrs;
		JSONArray jsonAttrs = o.optJSONArray("attributes");
		if (jsonAttrs != null) {
			attrs = new String[jsonAttrs.length()];
			for (int i = 0; i < jsonAttrs.length(); i++) {
				attrs[i] = jsonAttrs.getString(i);
			}
		} else {
			attrs = NO_ATTRS_VALUE;
		}
		return new WordEntry(word, definition, attrs);
	}
}
