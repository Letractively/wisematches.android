package wisematches.client.android.security.auth;

import android.accounts.Account;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class AccountSelectorDialog {
	private AccountSelectorDialog() {
	}

	public static AlertDialog chooseAccount(final Context context, final Account[] accounts, final DialogInterface.OnClickListener listener) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);

		final String[] names = new String[accounts.length];
		for (int i = 0; i < accounts.length; i++) {
			names[i] = accounts[i].name;
		}

		DialogInterface.OnClickListener l = new DialogInterface.OnClickListener() {
			public int mSelectedIndex = 0;

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case Dialog.BUTTON_NEGATIVE:
						dialog.cancel();
						break;

					case Dialog.BUTTON_POSITIVE:
						dialog.dismiss();
						listener.onClick(dialog, mSelectedIndex);
						break;

					default:
						mSelectedIndex = which;
						break;
				}
			}
		};

		builder.setTitle("Выберите аккаунт для входа");
		builder.setSingleChoiceItems(names, 0, l);
		builder.setPositiveButton("Войти", l);
		builder.setNegativeButton("Закрыть", l);

		return builder.create();
	}
}
