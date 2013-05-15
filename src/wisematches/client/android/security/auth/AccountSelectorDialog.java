package wisematches.client.android.security.auth;

import android.accounts.Account;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AccountSelectorDialog extends DialogFragment implements DialogInterface.OnClickListener {
	private Account[] accounts;
	private int mSelectedIndex;
	private OnDialogSelectorListener listener;

	public AccountSelectorDialog(Account[] accounts, int mSelectedIndex, OnDialogSelectorListener listener) {
		this.accounts = accounts;
		this.mSelectedIndex = mSelectedIndex;
		this.listener = listener;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

		final String[] names = new String[accounts.length];
		for (int i = 0; i < accounts.length; i++) {
			names[i] = accounts[i].name;
		}

		builder.setTitle("Выберите аккаунт для входа");
		builder.setSingleChoiceItems(names, mSelectedIndex, this);
		builder.setPositiveButton("Войти", this);
		builder.setNegativeButton("Закрыть", this);
		return builder.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
			case Dialog.BUTTON_NEGATIVE:
				dialog.cancel();
				break;

			case Dialog.BUTTON_POSITIVE:
				dialog.dismiss();
				// message selected value to registered calbacks
				listener.onSelectedOption(mSelectedIndex);
				break;

			default: // choice selected click
				mSelectedIndex = which;
				break;
		}
	}

	public interface OnDialogSelectorListener {
		public void onSelectedOption(int dialogId);
	}

	public static AccountSelectorDialog newInstance(Account[] accounts, int selected, OnDialogSelectorListener listener) {
		return new AccountSelectorDialog(accounts, selected, listener);
	}
}
