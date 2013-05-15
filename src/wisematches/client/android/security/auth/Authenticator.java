package wisematches.client.android.security.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.os.Bundle;
import wisematches.client.android.WiseMatchesApplication;
import wisematches.client.android.app.account.LoginActivity;
import wisematches.client.android.data.DataRequestManager;
import wisematches.client.android.data.model.person.Personality;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class Authenticator extends AbstractAccountAuthenticator {
	private final WiseMatchesApplication application;

	public static final String ACCOUNT_TYPE = "net.wisematches.android.auth";
	public static final String AUTH_TOKEN_TYPE = "net.wisematches.android.auth";

	public Authenticator(WiseMatchesApplication application) {
		super(application);
		this.application = application;
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response,
							 String accountType, String authTokenType,
							 String[] requiredFeatures, Bundle options) {
		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, LoginActivity.createIntent(application, response));
		return bundle;
	}

	@Override
	public Bundle confirmCredentials(final AccountAuthenticatorResponse response, final Account account, final Bundle options) {
		if (options != null && options.containsKey(AccountManager.KEY_PASSWORD)) {
			final String password = options.getString(AccountManager.KEY_PASSWORD);
			authenticate(account.name, password, application.getRequestManager(), new AuthenticationCallback() {
				@Override
				public void onResult(Bundle bundle) {
					if (bundle.getBoolean(AccountManager.KEY_BOOLEAN_RESULT)) {
						response.onResult(bundle);
					} else {
						final Bundle res = new Bundle();
						res.putParcelable(AccountManager.KEY_INTENT, LoginActivity.createIntent(application, account.name, bundle, response));
						response.onResult(res);
					}
				}
			});
			return null;
		} else {
			final Bundle res = new Bundle();
			res.putParcelable(AccountManager.KEY_INTENT, LoginActivity.createIntent(application, account.name, null, response));
			return res;
		}
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response,
								 String accountType) {
		final Bundle result = new Bundle();
		result.putString(AccountManager.KEY_ERROR_MESSAGE, "unsupported");
		return result;
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response,
							   Account account, String authTokenType, Bundle loginOptions) {
		final Bundle result = new Bundle();
		result.putString(AccountManager.KEY_ERROR_MESSAGE, "unsupported");
		return result;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response,
							  Account account, String[] features) {
		final Bundle result = new Bundle();
		result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
		return result;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response,
									Account account, String authTokenType, Bundle loginOptions) {
		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, LoginActivity.createIntent(application, account.name, null, response));
		return bundle;
	}

	public static void authenticate(final String username, final String password, DataRequestManager requestManager, final AuthenticationCallback callback) {
		requestManager.authenticate(username, password, new DataRequestManager.DataResponse<Personality>() {
			@Override
			public void onSuccess(Personality data) {
				final Bundle b = createBundle();
				b.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, true);
				b.putBundle(AccountManager.KEY_USERDATA, data.writeToBundle(new Bundle()));

				callback.onResult(b);
			}

			@Override
			public void onFailure(String code, String message) {
				final Bundle b = createBundle();
				b.putInt(AccountManager.KEY_ERROR_CODE, AccountManager.ERROR_CODE_INVALID_RESPONSE);
				b.putString(AccountManager.KEY_ERROR_MESSAGE, message);
				callback.onResult(b);
			}

			@Override
			public void onDataError() {
				final Bundle b = createBundle();
				b.putInt(AccountManager.KEY_ERROR_CODE, AccountManager.ERROR_CODE_BAD_REQUEST);
				callback.onResult(b);
			}

			@Override
			public void onConnectionError(int code) {
				final Bundle b = createBundle();
				if (code == 401) { // unauthorized
					b.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
				} else {
					b.putInt(AccountManager.KEY_ERROR_CODE, AccountManager.ERROR_CODE_BAD_ARGUMENTS);
				}
				callback.onResult(b);
			}


			private Bundle createBundle() {
				final Bundle b = new Bundle();
				b.putString(AccountManager.KEY_ACCOUNT_NAME, username);
				b.putString(AccountManager.KEY_AUTHTOKEN, password);
				return b;
			}
		});
	}

	public static interface AuthenticationCallback {
		void onResult(Bundle bundle);
	}
}
