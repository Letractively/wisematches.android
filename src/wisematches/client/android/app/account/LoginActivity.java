package wisematches.client.android.app.account;

import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesActivity;
import wisematches.client.android.security.auth.Authenticator;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class LoginActivity extends WiseMatchesActivity {
	private TextView errorField;

	private EditText usernameField;
	private EditText passwordField;

	private AccountAuthenticatorResponse authenticatorResponse = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_signin);

		getSupportActionBar().setTitle("Войти используя свой Аккаунт WiseMatches");

		final ClearErrorListener clearErrorListener = new ClearErrorListener();

		usernameField = (EditText) findViewById(R.id.accountFldEmail);
		usernameField.addTextChangedListener(clearErrorListener);

		passwordField = (EditText) findViewById(R.id.accountFldPwd);
		passwordField.addTextChangedListener(clearErrorListener);

		errorField = (TextView) findViewById(R.id.accountFldError);

		final Intent intent = getIntent();

		usernameField.setText(intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
		authenticatorResponse = intent.getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
		setErrorMessage(intent.getStringExtra(AccountManager.KEY_ERROR_MESSAGE));

		findViewById(R.id.accountBtnSignIn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				performAuthentication();
			}
		});

		findViewById(R.id.accountBtnRegister).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				performRegistration();
			}
		});
		authenticatorResponse.onRequestContinued();
	}

	private void performRegistration() {
		String nickname = "";
		String email = usernameField.getText().toString();
		if (email != null) {
			int i = email.indexOf("@");
			if (i > 0) {
				nickname = email.substring(0, i);
			}
		}
		startActivityForResult(RegisterActivity.createIntent(this, email, nickname), 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void performAuthentication() {
		final String username = usernameField.getText().toString();
		final String password = passwordField.getText().toString();

		Authenticator.authenticate(username, password, getRequestManager(), new Authenticator.AuthenticationCallback() {
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
				} else {
					boolean result = bundle.getBoolean(AccountManager.KEY_BOOLEAN_RESULT);
					if (result) {
						authenticatorResponse.onResult(bundle);
					} else {
						setErrorMessage("Неверное имя пользователя и пароль");
					}
				}
			}
		});
	}

	private void setErrorMessage(String msg) {
		if (msg == null) {
			errorField.setText("");
			errorField.setVisibility(View.GONE);
		} else {
			errorField.setText(msg);
			errorField.setVisibility(View.VISIBLE);
		}
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

	public static Intent createIntent(Context context, AccountAuthenticatorResponse response) {
		return createIntent(context, null, null, response);
	}

	public static Intent createIntent(Context context, String account, Bundle ops, AccountAuthenticatorResponse response) {
		final Intent intent = new Intent(context, LoginActivity.class);

		if (account != null) {
			intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account);
		}

		if (ops != null) {
			intent.putExtras(ops);
		}
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		return intent;
	}
}
