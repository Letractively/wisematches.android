package wisematches.client.android.app.account;

import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesApplication;
import wisematches.client.android.data.DataRequestManager;
import wisematches.client.android.data.model.Language;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.http.InfoWebView;
import wisematches.client.android.widget.LanguageAdapter;

import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RegisterActivity extends AuthenticationActivity {
	private EditText emailEditor;
	private EditText nicknameEditor;
	private Spinner languageEditor;
	private EditText passwordEditor;
	private EditText confirmationEditor;

	private Button registerButton;

	private InfoWebView infoWebView;

	private static final String INTENT_EXTRA_EMAIL = "ACCOUNT_EMAIL";
	private static final String INTENT_EXTRA_NICKNAME = "ACCOUNT_NICKNAME";

	public RegisterActivity() {
		super("Создание нового аккаунта WiseMatches", R.layout.account_register);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final ActionBar supportActionBar = getSupportActionBar();
		if (supportActionBar != null) {
			supportActionBar.setDisplayShowHomeEnabled(false);
		}

		infoWebView = (InfoWebView) findViewById(R.id.infoWebView);

		final TabHost host = (TabHost) findViewById(R.id.tabHost);
		if (host != null) {
			final TheTabContentFactory factory = new TheTabContentFactory();

			host.addTab(host.newTabSpec("terms").setIndicator("Условия Использования").setContent(factory));
			host.addTab(host.newTabSpec("policy").setIndicator("Политика Конфиденциальности").setContent(factory));
			host.addTab(host.newTabSpec("naming").setIndicator("Имя Пользователя").setContent(factory));
		}

		emailEditor = (EditText) findViewById(R.id.accountFldEmail);
		nicknameEditor = (EditText) findViewById(R.id.accountFldNickname);
		languageEditor = (Spinner) findViewById(R.id.accountFldLanguage);
		passwordEditor = (EditText) findViewById(R.id.accountFldPwd);
		confirmationEditor = (EditText) findViewById(R.id.accountFldPwdCnf);

		clearByChange(emailEditor, nicknameEditor, passwordEditor, confirmationEditor, languageEditor);

		final TextView termsView = (TextView) findViewById(R.id.accountFldTerms);
		termsView.setText(Html.fromHtml("Нажимая на кнопку 'Создать мой аккаунт' вы соглашаетесь с <a href=\"http://" + WiseMatchesApplication.WEB_HOST.toHostString() + "/info/terms\">'Условиями Использования'</a> и <a href=\"http://" + WiseMatchesApplication.WEB_HOST.toHostString() + "/info/policy\">'Политикой Конфиденциальности'</a>."));

		termsView.setMovementMethod(LinkMovementMethod.getInstance());

		languageEditor.setAdapter(new LanguageAdapter(this, android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item));

		final Intent intent = getIntent();
		final String email = intent.getStringExtra(INTENT_EXTRA_EMAIL);
		if (email != null) {
			emailEditor.setText(email);
		}

		final String nickname = intent.getStringExtra(INTENT_EXTRA_NICKNAME);
		if (nickname != null) {
			nicknameEditor.setText(nickname);
		}

		registerButton = (Button) findViewById(R.id.accountBtnRegister);
		registerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setControlsEnabled(false);
				performRegistration();
			}
		});
	}

	@Override
	protected void setControlsEnabled(boolean enabled) {
		super.setControlsEnabled(enabled);

		emailEditor.setEnabled(enabled);
		nicknameEditor.setEnabled(enabled);
		languageEditor.setEnabled(enabled);
		passwordEditor.setEnabled(enabled);
		confirmationEditor.setEnabled(enabled);

		registerButton.setEnabled(enabled);
	}

	private void performRegistration() {
		setControlsEnabled(false);

		final Language language = (Language) languageEditor.getSelectedItem();

		final String nickname = nicknameEditor.getText().toString();
		final String email = emailEditor.getText().toString();
		final String password = passwordEditor.getText().toString();
		final String confirm = confirmationEditor.getText().toString();
		final String timezone = TimeZone.getDefault().getID();

		getRequestManager().register(nickname, email, password, confirm, language.getCode(), timezone,
				new DataRequestManager.DataResponse<Personality>() {
					@Override
					public void onSuccess(Personality data) {
						performAuthentication(email, password);
					}

					@Override
					public void onFailure(String code, String message) {
						setErrorMessage(message);
						setControlsEnabled(true);
					}

					@Override
					public void onDataError() {
						setErrorMessage("Ошибка при работе с сервером");
						setControlsEnabled(true);
					}

					@Override
					public void onConnectionError(int code) {
						setErrorMessage("Ошибка при работе с сервером: " + code);
						setControlsEnabled(true);
					}
				});
	}

	public static Intent createIntent(Context context, String email, String nickname, boolean internal, AccountAuthenticatorResponse response) {
		final Intent intent = new Intent(context, RegisterActivity.class);
		if (email != null) {
			intent.putExtra(INTENT_EXTRA_EMAIL, email);
		}
		if (nickname != null) {
			intent.putExtra(INTENT_EXTRA_NICKNAME, nickname);
		}
		intent.putExtra(INTERNAL_ACCOUNT_REGISTRANT, internal);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		return intent;
	}

	private class TheTabContentFactory implements TabHost.TabContentFactory {
		@Override
		public View createTabContent(String tag) {
			final InfoWebView webView = new InfoWebView(RegisterActivity.this);
			webView.loadInfo("/info/" + tag);
			return webView;
		}
	}
}
