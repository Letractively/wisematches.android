package wisematches.client.android.data.qwe;

import org.json.JSONObject;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface ResponseParser<P> {
	P parseResponse(JSONObject object);
}
