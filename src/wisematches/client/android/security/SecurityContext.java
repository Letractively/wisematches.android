package wisematches.client.android.security;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.security.auth.AccountSelectorDialog;
import wisematches.client.android.security.auth.Authenticator;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SecurityContext implements CredentialsProvider {
	private SecuredPrincipal principal;

	public SecurityContext() {
	}

	public void authenticate(final Activity activity, final AuthorizationListener authorizationListener) {
		final Account[] accounts = Authenticator.getAccounts(activity);
		if (principal != null) {
			Account res = searchAccount(principal.credentials.getUserPrincipal().getName(), accounts);
			if (res != null) {
				authenticate(activity, res, authorizationListener);
				return;
			}
		}

		if (accounts.length == 0) {
			authenticate(activity, null, authorizationListener);
		} else if (accounts.length == 1) {
			authenticate(activity, accounts[0], authorizationListener);
		} else {
			AccountSelectorDialog.chooseAccount(activity, accounts, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					authenticate(activity, accounts[which], authorizationListener);
				}
			}).show();
		}
	}

	public Personality getPersonality() {
		return principal != null ? principal.personality : null;
	}

	@Override
	public void setCredentials(AuthScope authScope, Credentials credentials) {
		throw new UnsupportedOperationException("Authenticate method must be used instead");
	}

	@Override
	public Credentials getCredentials(AuthScope authScope) {
		return principal != null ? principal.credentials : null;
	}

	@Override
	public void clear() {
		this.principal = null;
	}


	private Account searchAccount(String name, Account[] accounts) {
		for (Account ac : accounts) {
			if (ac.name.equals(name)) {
				return ac;
			}
		}
		return null;
	}

	private void authenticate(Activity activity, Account account, AuthorizationListener authorizationListener) {
		clear();

		final TheAccountManagerCallback callback = new TheAccountManagerCallback(authorizationListener);
		if (account == null) {
			Authenticator.createAccount(activity, callback);
		} else {
			Authenticator.confirmAccount(activity, account, callback);
		}
	}


	/**
	 * @author Sergey Klimenko (smklimenko@gmail.com)
	 */
	public static interface AuthorizationListener {
		void authorized(Personality personality);
	}

	private static class SecuredPrincipal {
		private final Credentials credentials;
		private final Personality personality;

		private SecuredPrincipal(String account, String password, Personality personality) {
			this.credentials = new UsernamePasswordCredentials(account, password);
			this.personality = personality;
		}
	}

	private class TheAccountManagerCallback implements AccountManagerCallback<Bundle> {
		private final AuthorizationListener authorizationListener;

		private TheAccountManagerCallback(AuthorizationListener authorizationListener) {
			this.authorizationListener = authorizationListener;
		}

		@Override
		public void run(AccountManagerFuture<Bundle> future) {
			try {
				final Bundle result = future.getResult();
				if (result.getBoolean(AccountManager.KEY_BOOLEAN_RESULT)) {
					final String pwd = result.getString(AccountManager.KEY_PASSWORD);
					final String acc = result.getString(AccountManager.KEY_ACCOUNT_NAME);
					final Personality personality = new Personality(result.getBundle(AccountManager.KEY_USERDATA));
					SecurityContext.this.principal = new SecuredPrincipal(acc, pwd, personality);
					if (authorizationListener != null) {
						authorizationListener.authorized(personality);
					}
				} else {
					if (authorizationListener != null) {
						authorizationListener.authorized(null);
					}
				}
			} catch (Exception ex) {
				if (authorizationListener != null) {
					authorizationListener.authorized(null);
				}
			}
		}
	}
}
