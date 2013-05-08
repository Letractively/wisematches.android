package wisematches.client.android.data.qwe;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.foxykeep.datadroid.exception.ConnectionException;
import com.foxykeep.datadroid.exception.DataException;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class HttpClientManager {
	private final HttpClient client;
	private final CookieStore cookieStore = new BasicCookieStore();
	private final HttpContext localContext = new BasicHttpContext();

	private static final int DEFAULT_TIMEOUT = 3000;

	private static final HttpHost WISEMATCHES_HOST = new HttpHost("www.wisematches.net", 80);

	public HttpClientManager() {
		final HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.USER_AGENT, "Wisematches/1.0");
		HttpClientParams.setRedirecting(params, false);
		HttpConnectionParams.setSoTimeout(params, DEFAULT_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(params, DEFAULT_TIMEOUT);

		client = new DefaultHttpClient(params);
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	}

	public void release() {
		cookieStore.clear();
	}

	/**
	 * @deprecated move to other place.
	 */
	@Deprecated
	public static void browse(Context context, String uri) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + getServerHost() + uri));
		context.startActivity(Intent.createChooser(intent, "Chose browser"));
	}

	public Response execute(String url, Header... headers) throws ConnectionException, DataException {
		return execute(url, null, null, headers);
	}

	public Response execute(String url, JSONObject data, Header... headers) throws ConnectionException, DataException {
		return execute(url, null, data, headers);
	}

	public Response execute(String url, HttpParams params, Header... headers) throws ConnectionException, DataException {
		return execute(url, params, null, headers);
	}

	public Response execute(String url, HttpParams params, JSONObject data, Header... headers) throws ConnectionException, DataException {
		try {
			final HttpPost request = new HttpPost(url);

			if (params != null) {
				request.setParams(params);
			}

			if (data != null) {
				request.setEntity(new StringEntity(data.toString(), "UTF-8"));
			}

			if (headers != null) {
				request.setHeaders(headers);
			}

			HttpConnectionParams.setSoTimeout(request.getParams(), DEFAULT_TIMEOUT);
			HttpConnectionParams.setConnectionTimeout(request.getParams(), DEFAULT_TIMEOUT);

			final HttpResponse response = client.execute(WISEMATCHES_HOST, request, localContext);
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
			return new Response(r.getBoolean("success"), r.optJSONObject("data"));
		} catch (JSONException ex) {
			throw new DataException(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new ConnectionException(ex.getMessage(), 503);
		}
	}


	private static String getServerHost() {
		return WISEMATCHES_HOST.getHostName() + (WISEMATCHES_HOST.getPort() == -1 ? "" : ":" + WISEMATCHES_HOST.getPort());
	}

	/**
	 * @author Sergey Klimenko (smklimenko@gmail.com)
	 */
	public static final class Response {
		private final boolean success;
		private final JSONObject data;

		public Response(boolean success, JSONObject data) {
			this.data = data;
			this.success = success;
		}

		public boolean isSuccess() {
			return success;
		}

		public JSONObject getData() {
			return data;
		}
	}
}
