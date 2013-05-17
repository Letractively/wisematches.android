package wisematches.client.android.app.account;

import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesActivity;
import wisematches.client.android.security.auth.Authenticator;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AuthenticationActivity extends WiseMatchesActivity {
	private TextView errorField;
	private ProgressBar progressBar;

	private boolean internalRegistrant;

	protected AccountAuthenticatorResponse authenticatorResponse;

	private final int contentView;
	private final ClearErrorListener clearErrorListener = new ClearErrorListener();

	protected static final String INTERNAL_ACCOUNT_REGISTRANT = "INTERNAL_ACCOUNT_REGISTRANT";

	public AuthenticationActivity(int contentView) {
		this.contentView = contentView;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(contentView);

		errorField = (TextView) findViewById(R.id.accountFldError);

		final Intent intent = getIntent();
		authenticatorResponse = intent.getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
		authenticatorResponse.onRequestContinued();

		progressBar = (ProgressBar) findViewById(R.id.accountFldProgress);

		internalRegistrant = intent.getBooleanExtra(INTERNAL_ACCOUNT_REGISTRANT, false);
		setErrorMessage(intent.getStringExtra(AccountManager.KEY_ERROR_MESSAGE));
	}

	protected void setErrorMessage(String msg) {
		if (msg == null) {
			errorField.setText("");
			errorField.setVisibility(View.GONE);
		} else {
			errorField.setText(Html.fromHtml(msg));
			errorField.setVisibility(View.VISIBLE);
		}
	}

	protected void clearByChange(View... textView) {
		for (View view : textView) {
			if (view instanceof TextView) {
				((TextView) view).addTextChangedListener(clearErrorListener);
			}
		}
	}

	protected void setControlsEnabled(boolean enabled) {
		if (enabled) {
			progressBar.setVisibility(View.INVISIBLE);
		} else {
			setErrorMessage(null);
			progressBar.setVisibility(View.VISIBLE);
		}
	}

	protected boolean isInternalRegistrant() {
		return internalRegistrant;
	}

	protected void performAuthentication(final String email, final String password) {
		setControlsEnabled(false);
		Authenticator.authenticate(email, password, getRequestManager(), new Authenticator.AuthenticationCallback() {
			@Override
			public void onResult(Bundle bundle) {
				int errorCode = bundle.getInt(AccountManager.KEY_ERROR_CODE);
				if (errorCode != 0) {
					String msg = bundle.getString(AccountManager.KEY_ERROR_MESSAGE);
					if (msg != null) {
						setErrorMessage(msg);
					} else {
						setErrorMessage("Ошибка работы с сервером. Попробуйте позже.");
					}
					setControlsEnabled(true);
				} else {
					boolean result = bundle.getBoolean(AccountManager.KEY_BOOLEAN_RESULT);
					if (result) {
						if (email != null && password != null) {
							Authenticator.updateAccountExplicitly(AuthenticationActivity.this, email, password);
						}
						authenticatorResponse.onResult(bundle);
						finish();
					} else {
						setErrorMessage("Неверное имя пользователя и пароль");
						setControlsEnabled(true);
					}
				}
			}
		});
	}

	private final class ClearErrorListener implements TextWatcher {
		private ClearErrorListener() {
		}

		@Override
		public void afterTextChanged(Editable editable) {
		}

		@Override
		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			setErrorMessage(null);
		}

		@Override
		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		}
	}
}
