package wisematches.client.android.security.auth;

import android.accounts.*;
import android.app.Activity;
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

	private static final String ACCOUNT_TYPE = "net.wisematches.android.auth";
	private static final String AUTH_TOKEN_TYPE = "net.wisematches.android.auth";

	private static final String INTERNAL_AUTH_FLAG = "INTERNAL_AUTH_FLAG";

	public Authenticator(WiseMatchesApplication application) {
		super(application);
		this.application = application;
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response,
							 String accountType, String authTokenType,
							 String[] requiredFeatures, Bundle options) {
		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, LoginActivity.createIntent(application, null, getInternalAuthFlag(options), response));
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
						res.putParcelable(AccountManager.KEY_INTENT, LoginActivity.createIntent(application, account.name, getInternalAuthFlag(options), response));
						response.onResult(res);
					}
				}
			});
			return null;
		} else {
			final Bundle res = new Bundle();
			res.putParcelable(AccountManager.KEY_INTENT, LoginActivity.createIntent(application, account.name, getInternalAuthFlag(options), response));
			return res;
		}
	}


	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response,
									Account account, String authTokenType, Bundle options) {
		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, LoginActivity.createIntent(application, account.name, getInternalAuthFlag(options), response));
		return bundle;
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

	public static Account[] getAccounts(Activity activity) {
		return AccountManager.get(activity).getAccountsByType(Authenticator.ACCOUNT_TYPE);
	}

	public static void createAccount(Activity activity, AccountManagerCallback<Bundle> callback) {
		final Bundle b = new Bundle();
		setInternalAuthFlag(b, true);
		AccountManager.get(activity).addAccount(Authenticator.ACCOUNT_TYPE, Authenticator.AUTH_TOKEN_TYPE, null, b, activity, callback, null);
	}

	public static void confirmAccount(Activity activity, Account account, AccountManagerCallback<Bundle> callback) {
		final AccountManager accountManager = AccountManager.get(activity);

		final Bundle ops = new Bundle();
		setInternalAuthFlag(ops, true);
		ops.putString(AccountManager.KEY_PASSWORD, accountManager.getPassword(account));
		accountManager.confirmCredentials(account, ops, activity, callback, null);
	}

	public static void updateAccountExplicitly(Activity activity, String login, String password) {
		final AccountManager manager = AccountManager.get(activity);

		Account account = null;
		Account[] accountsByType = manager.getAccountsByType(ACCOUNT_TYPE);
		for (int i = 0; i < accountsByType.length && account == null; i++) {
			Account ac = accountsByType[i];
			if (ac.name.equals(login)) {
				account = ac;
			}
		}

		if (account == null) {
			manager.addAccountExplicitly(new Account(login, ACCOUNT_TYPE), password, null);
		} else {
			manager.setPassword(account, password);
		}
	}

	public static void authenticate(final String username, final String password, DataRequestManager requestManager, final AuthenticationCallback callback) {
		requestManager.login(username, password, new DataRequestManager.DataResponse<Personality>() {
			@Override
			public void onSuccess(Personality data) {
				final Bundle b = createBundle();
				b.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, true);
				b.putBundle(AccountManager.KEY_USERDATA, data.writeToBundle(new Bundle()));
				b.putString(AccountManager.KEY_PASSWORD, password);

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

	private static boolean getInternalAuthFlag(Bundle options) {
		return options != null && options.getBoolean(INTERNAL_AUTH_FLAG);
	}

	private static void setInternalAuthFlag(Bundle options, boolean flag) {
		if (options != null) {
			options.putBoolean(INTERNAL_AUTH_FLAG, flag);
		}
	}

	public static interface AuthenticationCallback {
		void onResult(Bundle bundle);
	}
}
