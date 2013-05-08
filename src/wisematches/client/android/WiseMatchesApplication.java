package wisematches.client.android;

import android.app.Application;
import android.content.Context;
import com.foxykeep.datadroid.requestmanager.RequestManager;
import wisematches.client.android.app.account.model.Player;
import wisematches.client.android.data.service.http.HttpRequestService;
import wisematches.client.android.graphics.BitmapFactory;

import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WiseMatchesApplication extends Application {
	private Principal principal;
	private BitmapFactory bitmapFactory;

	private RequestManager requestManager;

	public WiseMatchesApplication() {
	}


	@Override
	public void onCreate() {
		super.onCreate();

		this.bitmapFactory = new BitmapFactory(getResources());
		this.requestManager = new WMRequestManager(this.getApplicationContext());
	}

	@Override
	public void onTerminate() {
		super.onTerminate();

		this.principal = null;
		this.requestManager = null;

		bitmapFactory.terminate();
//		wiseMatchesServer.release();
	}


	public RequestManager getRequestManager() {
		return requestManager;
	}

	@Deprecated
	public Principal getPrincipal() {
		return principal;
	}

	public BitmapFactory getBitmapFactory() {
		return bitmapFactory;
	}

/*
	@Deprecated
	public Principal authenticate() throws CommunicationException, CooperationException {
		final SharedPreferences preferences = getSharedPreferences("principal", Context.MODE_PRIVATE);
		final String credentials = preferences.getString("credentials", null);
		if (credentials != null) {
			this.principal = authImpl(credentials);
			return principal;
		}
		return null;
	}

	@Deprecated
	public Principal authenticate(String username, String password) throws CommunicationException, CooperationException {
		final String credentials = Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
		this.principal = authImpl(credentials);
		return this.principal;
	}


	@Deprecated
	private Principal authImpl(String credentials) throws CommunicationException, CooperationException {
		final BasicHeader basicHeader = new BasicHeader("Authorization", "Basic " + credentials);
		final NetworkConnection.Response r = wiseMatchesServer.execute("/account/login.ajax", basicHeader);

		try {
			final JSONObject data = r.getData();
			Principal player = new Principal(
					data.getLong("id"),
					data.getString("nickname"),
					data.getString("language"),
					TimeZone.getTimeZone(data.getString("timeZone")),
					data.getString("type"),
					data.optString("membership", null),
					true);

			final SharedPreferences preferences = getSharedPreferences("principal", Context.MODE_PRIVATE);
			final SharedPreferences.Editor edit = preferences.edit();
			edit.putLong("id", player.getId());
			edit.putString("credentials", credentials);
			edit.commit();
			return player;
		} catch (JSONException ex) {
			throw new CooperationException(ex.getMessage(), ex);
		}
	}

	*/

	/**
	 * @author Sergey Klimenko (smklimenko@gmail.com)
	 */
	@Deprecated
	public static final class Principal implements Player {
		private final long id;
		private final String nickname;
		private final String language;
		private final TimeZone timeZone;

		private final String type;
		private final String membership;

		private final boolean online;

		public Principal(long id, String nickname, String language, TimeZone timeZone, String type, String membership, boolean online) {
			this.id = id;
			this.nickname = nickname;
			this.language = language;
			this.timeZone = timeZone;
			this.type = type;
			this.membership = membership;
			this.online = online;
		}

		@Override
		public long getId() {
			return id;
		}

		@Override
		public String getNickname() {
			return nickname;
		}

		public String getLanguage() {
			return language;
		}

		public TimeZone getTimeZone() {
			return timeZone;
		}

		@Override
		public String getType() {
			return type;
		}

		@Override
		public String getMembership() {
			return membership;
		}

		@Override
		public boolean isOnline() {
			return online;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("Player{");
			sb.append("id=").append(id);
			sb.append(", nickname='").append(nickname).append('\'');
			sb.append(", language='").append(language).append('\'');
			sb.append(", timeZone=").append(timeZone);
			sb.append(", type='").append(type).append('\'');
			sb.append(", membership='").append(membership).append('\'');
			sb.append(", online=").append(online);
			sb.append('}');
			return sb.toString();
		}
	}

	private static class WMRequestManager extends RequestManager {
		private WMRequestManager(Context context) {
			super(context, HttpRequestService.class);
		}
	}
}