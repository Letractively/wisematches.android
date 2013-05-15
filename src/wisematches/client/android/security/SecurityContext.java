package wisematches.client.android.security;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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

	public void authenticate(final FragmentActivity activity, final AuthorizationListener authorizationListener) {
		final AccountManager accountManager = AccountManager.get(activity);

		final Account[] accounts = accountManager.getAccountsByType(Authenticator.ACCOUNT_TYPE);
		if (account != null) {
			Account res = searchAccount(account.name, accounts);
			if (res != null) {
				authenticate(activity, accountManager, res, authorizationListener);
				return;
			}
		}

		if (accounts.length == 0) {
			authenticate(activity, accountManager, null, authorizationListener);
		} else if (accounts.length == 1) {
			authenticate(activity, accountManager, accounts[0], authorizationListener);
		} else {
			AccountSelectorDialog.newInstance(accounts, 0, new AccountSelectorDialog.OnDialogSelectorListener() {
				@Override
				public void onSelectedOption(int dialogId) {
					authenticate(activity, accountManager, accounts[dialogId], authorizationListener);
				}
			}).show(activity.getFragmentManager(), "WM");
		}
	}

	public void clear() {
		this.account = null;
		this.personality = null;
	}


	public Personality getPersonality() {
		return personality;
	}

	private void authenticate(Activity activity, AccountManager accountManager, Account account, AuthorizationListener authorizationListener) {
		clear();

		final TheAccountManagerCallback callback = new TheAccountManagerCallback(account, authorizationListener);
		if (account == null) {
			accountManager.addAccount(Authenticator.ACCOUNT_TYPE, Authenticator.AUTH_TOKEN_TYPE, null, null, activity, callback, null);
		} else {
			final Bundle ops = new Bundle();
			ops.putString(AccountManager.KEY_PASSWORD, accountManager.getPassword(account));
			accountManager.confirmCredentials(account, ops, activity, callback, null);
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
