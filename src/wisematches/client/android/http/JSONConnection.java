package wisematches.client.android.http;

import com.foxykeep.datadroid.exception.ConnectionException;
import com.foxykeep.datadroid.exception.DataException;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.*;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.WiseMatchesApplication;
import wisematches.client.android.security.SecurityContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class JSONConnection {
	private final DefaultHttpClient client;
	private final BasicCookieStore cookieStore = new BasicCookieStore();
	private final BasicHttpContext localContext = new BasicHttpContext();

	private static final int DEFAULT_TIMEOUT = 10000;

	public JSONConnection(SecurityContext securityContext) {
		final HttpParams params = new BasicHttpParams();
		params.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		params.setParameter(CoreProtocolPNames.USER_AGENT, "Wisematches/1.0");

		HttpClientParams.setRedirecting(params, true);
		HttpConnectionParams.setSoTimeout(params, DEFAULT_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(params, DEFAULT_TIMEOUT);

		client = new DefaultHttpClient(params);
		client.setCredentialsProvider(securityContext);
		client.addRequestInterceptor(new HttpRequestInterceptor() {
			@Override
			public void process(HttpRequest httpRequest, HttpContext context) throws HttpException, IOException {
				AuthState state = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
				if (state.getAuthScheme() == null) {
					BasicScheme scheme = new BasicScheme();
					CredentialsProvider credentialsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
					Credentials credentials = credentialsProvider.getCredentials(AuthScope.ANY);
					if (credentials != null) {
						state.setAuthScope(AuthScope.ANY);
						state.setAuthScheme(scheme);
						state.setCredentials(credentials);
					}
				}
			}
		});

		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	}

	public Response execute(String url, Header[] headers, HttpParams params, JSONObject data) throws ConnectionException, DataException {
		final HttpPost request = new HttpPost(url);

		if (params != null) {
			request.setParams(params);
		}

		if (data != null) {
			try {
				request.setEntity(new StringEntity(data.toString(), HTTP.UTF_8));
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
			if (request instanceof HttpPost) {
				request.setHeader("Content-type", "application/json");
			} else {
				request.setHeader("Content-type", "application/x-www-form-urlencoded");
			}
			request.setHeader("Accept-Language", "ru");

			final HttpParams params = request.getParams();
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			HttpProtocolParams.setHttpElementCharset(params, HTTP.UTF_8);
			HttpConnectionParams.setSoTimeout(params, DEFAULT_TIMEOUT);
			HttpConnectionParams.setConnectionTimeout(params, DEFAULT_TIMEOUT);

			final HttpResponse response = client.execute(WiseMatchesApplication.WEB_HOST, request, localContext);
			final StatusLine status = response.getStatusLine();
			final String content = EntityUtils.toString(response.getEntity());
			if (status.getStatusCode() != 200) {
				throw new ConnectionException(content, status.getStatusCode());
			}

			if (content == null || content.length() == 0) {
				return null;
			}

			final JSONObject r = new JSONObject(content);
			Object data = r.opt("data");
			if (data == JSONObject.NULL) {
				data = null;
			}
			return new Response(r.getBoolean("success"), data, r.optString("code"), r.optString("message"));
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
		private final Object data;
		private final String errorCode;
		private final String errorMessage;

		public Response(boolean success, Object data, String errorCode, String errorMessage) {
			this.success = success;
			this.data = data;
			this.errorCode = errorCode;
			this.errorMessage = errorMessage;
		}

		public boolean isSuccess() {
			return success;
		}

		public Object getData() {
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
