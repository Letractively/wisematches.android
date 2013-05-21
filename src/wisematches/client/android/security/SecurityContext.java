package wisematches.client.android.security;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.security.auth.AccountSelectorDialog;
import wisematches.client.android.security.auth.Authenticator;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SecurityContext {
	private Account account;
	private Personality personality;

	public SecurityContext() {
	}

	public void authenticate(final Activity activity, final AuthorizationListener authorizationListener) {
		final Account[] accounts = Authenticator.getAccounts(activity);
		if (account != null) {
			Account res = searchAccount(account.name, accounts);
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

	public void clear() {
		this.account = null;
		this.personality = null;
	}


	public Personality getPersonality() {
		return personality;
	}

	private void authenticate(Activity activity, Account account, AuthorizationListener authorizationListener) {
		clear();

		final TheAccountManagerCallback callback = new TheAccountManagerCallback(account, authorizationListener);
		if (account == null) {
			Authenticator.createAccount(activity, callback);
		} else {
			Authenticator.confirmAccount(activity, account, callback);
		}
	}

	private Account searchAccount(String name, Account[] accounts) {
		for (Account ac : accounts) {
			if (ac.name.equals(name)) {
				return account;
			}
		}
		return null;
	}

	private class TheAccountManagerCallback implements AccountManagerCallback<Bundle> {
		private final Account account;
		private final AuthorizationListener authorizationListener;

		private TheAccountManagerCallback(Account account, AuthorizationListener authorizationListener) {
			this.account = account;
			this.authorizationListener = authorizationListener;
		}

		@Override
		public void run(AccountManagerFuture<Bundle> future) {
			try {
				final Bundle result = future.getResult();
				if (result.getBoolean(AccountManager.KEY_BOOLEAN_RESULT)) {
					SecurityContext.this.account = this.account;
					SecurityContext.this.personality = new Personality(result.getBundle(AccountManager.KEY_USERDATA));
					authorizationListener.authorized(personality);
				} else {
					authorizationListener.authorized(null);
				}
			} catch (Exception ex) {
				authorizationListener.authorized(null);
			}
		}
	}

	/**
	 * @author Sergey Klimenko (smklimenko@gmail.com)
	 */
	public static interface AuthorizationListener {
		void authorized(Personality personality);
	}
}
