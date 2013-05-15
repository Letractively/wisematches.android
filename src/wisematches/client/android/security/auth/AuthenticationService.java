package wisematches.client.android.security.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import wisematches.client.android.WiseMatchesApplication;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AuthenticationService extends Service {
	private Authenticator mAuthenticator;

	@Override
	public void onCreate() {
		mAuthenticator = new Authenticator((WiseMatchesApplication) getApplication());
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mAuthenticator.getIBinder();
	}

	@Override
	public void onDestroy() {
	}
}
