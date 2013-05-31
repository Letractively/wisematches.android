package wisematches.client.android.data.service.operation;

import android.os.Parcelable;
import com.foxykeep.datadroid.exception.ConnectionException;
import com.foxykeep.datadroid.exception.CustomRequestException;
import com.foxykeep.datadroid.exception.DataException;
import com.foxykeep.datadroid.requestmanager.Request;
import org.apache.http.HttpRequest;
import org.json.JSONException;
import wisematches.client.android.data.service.RequestRejectedException;
import wisematches.client.android.http.JSONConnection;

import java.util.ArrayList;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class JSONOperation<P> {
	private JSONOperation() {
	}

	public final P execute(Request request, JSONConnection connection) throws ConnectionException, DataException, CustomRequestException {
		try {
			final JSONConnection.Response response = connection.execute(createRequest(request));
			if (!response.isSuccess()) {
				throw new RequestRejectedException(response.getErrorCode(), response.getErrorMessage());
			}
			final Object data = response.getData();
			return data == null ? null : createResponse(data);
		} catch (JSONException ex) {
			throw new DataException("Response can't be parsed: " + ex.getMessage(), ex);
		}
	}


	protected abstract HttpRequest createRequest(Request request) throws DataException;

	protected abstract P createResponse(Object data) throws JSONException;


	public static abstract class List<P extends Parcelable> extends JSONOperation<ArrayList<P>> {
		protected List() {
		}
	}

	public static abstract class Primitive<P extends Parcelable> extends JSONOperation<P> {
		protected Primitive() {
		}
	}
}