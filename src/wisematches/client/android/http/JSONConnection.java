package wisematches.client.android.http;

import com.foxykeep.datadroid.exception.ConnectionException;
import com.foxykeep.datadroid.exception.DataException;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.WiseMatchesApplication;

import java.io.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class JSONConnection {
	private final HttpClient client;
	private final CookieStore cookieStore = new BasicCookieStore();
	private final HttpContext localContext = new BasicHttpContext();

	private static final int DEFAULT_TIMEOUT = 60000;

	public JSONConnection() {
		final HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.USER_AGENT, "Wisematches/1.0");

		HttpClientParams.setRedirecting(params, false);
		HttpConnectionParams.setSoTimeout(params, DEFAULT_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(params, DEFAULT_TIMEOUT);

		client = new DefaultHttpClient(params);
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	}


	public Response execute(String url, Header[] headers) throws ConnectionException, DataException {
		return execute(url, headers, null, null);
	}

	public Response execute(String url, JSONObject data) throws ConnectionException, DataException {
		return execute(url, null, null, data);
	}

	public Response execute(String url, Header[] headers, JSONObject data) throws ConnectionException, DataException {
		return execute(url, headers, null, data);
	}

	public Response execute(String url, HttpParams params) throws ConnectionException, DataException {
		return execute(url, null, params, null);
	}

	public Response execute(String url, Header[] headers, HttpParams params) throws ConnectionException, DataException {
		return execute(url, headers, params, null);
	}

	public Response execute(String url, HttpParams params, JSONObject data) throws ConnectionException, DataException {
		return execute(url, null, params, data);
	}

	public Response execute(String url, Header[] headers, HttpParams params, JSONObject data) throws ConnectionException, DataException {
		final HttpPost request = new HttpPost(url);

		if (params != null) {
			request.setParams(params);
		}

		if (data != null) {
			try {
				request.setEntity(new StringEntity(data.toString(), "UTF-8"));
			} catch (UnsupportedEncodingException ex) {
				throw new DataException("Unsupported encoding UTF-8", ex);
			}
		}

		if (headers != null) {
			request.setHeaders(headers);
		}

		return execute(request);
	}

	public Response execute(HttpRequest request) throws ConnectionException, DataException {
		try {
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");

			HttpConnectionParams.setSoTimeout(request.getParams(), DEFAULT_TIMEOUT);
			HttpConnectionParams.setConnectionTimeout(request.getParams(), DEFAULT_TIMEOUT);

			final HttpResponse response = client.execute(WiseMatchesApplication.WEB_HOST, request, localContext);
			final StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 200) {
				throw new ConnectionException(status.getReasonPhrase(), status.getStatusCode());
			}

			final HttpEntity entity = response.getEntity();
			final InputStream content = entity.getContent();

			String line;
			final StringBuilder builder = new StringBuilder();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			if (builder.length() == 0) {
				return null;
			}

			final JSONObject r = new JSONObject(builder.toString());
			return new Response(r.getBoolean("success"), r.optJSONObject("data"), r.optString("code"), r.optString("message"));
		} catch (JSONException ex) {
			throw new DataException(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new ConnectionException(ex.getMessage(), ex);
		}
	}


	public void release() {
		this.cookieStore.clear();

		this.client.getConnectionManager().shutdown();
	}

	/**
	 * @author Sergey Klimenko (smklimenko@gmail.com)
	 */
	public static class Response {
		private final boolean success;
		private final JSONObject data;
		private final String errorCode;
		private final String errorMessage;

		public Response(boolean success, JSONObject data, String errorCode, String errorMessage) {
			this.success = success;
			this.data = data;
			this.errorCode = errorCode;
			this.errorMessage = errorMessage;
		}

		public boolean isSuccess() {
			return success;
		}

		public JSONObject getData() {
			return data;
		}

		public String getErrorCode() {
			return errorCode;
		}

		public String getErrorMessage() {
			return errorMessage;
		}
	}
}
