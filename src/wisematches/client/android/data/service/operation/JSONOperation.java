package wisematches.client.android.data.service.operation;

import android.os.Parcelable;
import com.foxykeep.datadroid.exception.ConnectionException;
import com.foxykeep.datadroid.exception.CustomRequestException;
import com.foxykeep.datadroid.exception.DataException;
import com.foxykeep.datadroid.requestmanager.Request;
import org.apache.http.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.service.RequestRejectedException;
import wisematches.client.android.http.WebConnection;

import java.util.ArrayList;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class JSONOperation<P> {
	private JSONOperation() {
	}

	public final P execute(Request request, WebConnection connection) throws ConnectionException, DataException, CustomRequestException {
		try {
			final String content = connection.execute(createRequest(request));
			final Object data = parseResponseData(content);
			if (data != null) {
				return createResponse(data);
			}
			return null;
		} catch (JSONException ex) {
			throw new DataException("Response can't be parsed: " + ex.getMessage(), ex);
		}
	}

	protected abstract HttpRequest createRequest(Request request) throws DataException;

	protected abstract P createResponse(Object data) throws JSONException;

	protected Object parseResponseData(String content) throws JSONException, RequestRejectedException {
		final JSONObject r = new JSONObject(content);
		if (!r.getBoolean("success")) {
			throw new RequestRejectedException(r.optString("code"), r.optString("message"));
		}

		Object data = r.opt("data");
		if (data == JSONObject.NULL) {
			return null;
		}
		return data;
	}

	public static abstract class List<P extends Parcelable> extends JSONOperation<ArrayList<P>> {
		protected List() {
		}
	}

	public static abstract class Primitive<P extends Parcelable> extends JSONOperation<P> {
		protected Primitive() {
		}
	}
}