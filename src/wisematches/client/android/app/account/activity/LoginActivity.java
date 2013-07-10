package wisematches.client.android.app.account.activity;

import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.actionbarsherlock.app.ActionBar;
import wisematches.client.android.R;
import wisematches.client.android.http.InfoWebView;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class LoginActivity extends AuthenticationActivity {
	private Button signInButton;
	private Button visitorButton;
	private Button registerButton;

	private EditText usernameField;
	private EditText passwordField;

	public LoginActivity() {
		super("Вход с помощью Аккаунта WiseMatches", R.layout.account_signin);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final ActionBar supportActionBar = getSupportActionBar();
		if (supportActionBar != null) {
			supportActionBar.setDisplayShowHomeEnabled(false);
		}

		final InfoWebView infoTextView = (InfoWebView) findViewById(R.id.infoWebView);
		if (infoTextView != null) {
			infoTextView.showPage("general", getRequestManager());
		}

		usernameField = (EditText) findViewById(R.id.accountFldEmail);
		passwordField = (EditText) findViewById(R.id.accountFldPwd);

		signInButton = (Button) findViewById(R.id.accountBtnSignIn);
		visitorButton = (Button) findViewById(R.id.accountBtnVisitor);
		registerButton = (Button) findViewById(R.id.accountBtnRegister);

		final Intent intent = getIntent();
		usernameField.setText(intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));

		clearByChange(usernameField, passwordField, signInButton, visitorButton, registerButton);

		if (!isInternalRegistrant()) {
			visitorButton.setVisibility(View.GONE);
		}

		signInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setControlsEnabled(false);
				performAuthentication(LoginActivity.this.usernameField.getText().toString(), LoginActivity.this.passwordField.getText().toString());
			}
		});

		visitorButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setControlsEnabled(false);
				performAuthentication(null, null);
			}
		});

		registerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				performRegistration();
			}
		});
	}

	@Override
	protected void setControlsEnabled(boolean enabled) {
		super.setControlsEnabled(enabled);

		usernameField.setEnabled(enabled);
		passwordField.setEnabled(enabled);

		signInButton.setEnabled(enabled);
		visitorButton.setEnabled(enabled);
		registerButton.setEnabled(enabled);
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
		startActivity(RegisterActivity.createIntent(this, email, nickname, isInternalRegistrant(), authenticatorResponse));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static Intent createIntent(Context context, String account, boolean internal, AccountAuthenticatorResponse response) {
		final Intent intent = new Intent(context, LoginActivity.class);

		if (account != null) {
			intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account);
		}

		intent.putExtra(INTERNAL_ACCOUNT_REGISTRANT, internal);

		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		return intent;
	}
}
