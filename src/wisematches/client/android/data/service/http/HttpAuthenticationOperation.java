package wisematches.client.android.data.service.http;

import android.content.Context;
import android.util.Base64;
import com.foxykeep.datadroid.exception.ConnectionException;
import com.foxykeep.datadroid.exception.CustomRequestException;
import com.foxykeep.datadroid.exception.DataException;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.qwe.RequestType;
import wisematches.client.android.data.service.AbstractRequestService;
import wisematches.client.android.data.service.AuthenticationOperation;

import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HttpAuthenticationOperation extends AuthenticationOperation {
	private HttpRequestService requestService;

	public HttpAuthenticationOperation() {
	}

    @Override
    protected Personality execute(Context context, String username, String password) throws ConnectionException, DataException, CustomRequestException {
        throw new UnsupportedOperationException("TODO: not implemented");
    }

    /*
        @Override
        protected void onCreate(AbstractRequestService requestService) {
            super.onCreate(requestService);

            this.requestService = (HttpRequestService) requestService;
        }

        @Override
        protected Personality execute(Context context, String username, String password)
                throws ConnectionException, DataException {
            final String credentials = Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
            final BasicHeader basicHeader = new BasicHeader("Authorization", "Basic " + credentials);
            WiseMatchesWebServer.Response r = requestService.getWebServer().execute("/account/login.ajax", basicHeader);
            try {
                if (!r.isSuccess()) {
                    throw new DataException("Request is not success.");
                }

                final JSONObject data = r.getData();
                return new Personality(
                        data.getLong("id"),
                        data.getString("nickname"),
                        data.getString("language"),
                        TimeZone.getTimeZone(data.getString("timeZone")),
                        data.getString("type"),
                        data.optString("membership", null),
                        true);
            } catch (JSONException ex) {
                throw new DataException(ex.getMessage(), ex);
            }
        }

    */
}
