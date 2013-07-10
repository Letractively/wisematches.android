package wisematches.client.android.app.account.activity;

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
import wisematches.client.android.data.model.info.InfoPage;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.http.InfoWebView;
import wisematches.client.android.widget.LanguageAdapter;

import java.util.HashMap;
import java.util.Map;
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

	private Button signInButton;
	private Button visitorButton;
	private Button registerButton;

	private TabHost tabHost;

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

		tabHost = (TabHost) findViewById(R.id.tabHost);
		if (tabHost != null) {
			tabHost.setup();

			final InfoWebView infoWebView = (InfoWebView) findViewById(R.id.infoWebView);
			final TheTabChangeListener tabChangeListener = new TheTabChangeListener(infoWebView);

			tabHost.setOnTabChangedListener(tabChangeListener);

			tabHost.addTab(tabHost.newTabSpec("terms").setIndicator("Условия Использования").setContent(R.id.infoWebView));
			tabHost.addTab(tabHost.newTabSpec("policy").setIndicator("Политика Конфиденциальности").setContent(R.id.infoWebView));
			tabHost.addTab(tabHost.newTabSpec("naming").setIndicator("Имя Пользователя").setContent(R.id.infoWebView));
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

		signInButton = (Button) findViewById(R.id.accountBtnSignIn);
		visitorButton = (Button) findViewById(R.id.accountBtnVisitor);
		registerButton = (Button) findViewById(R.id.accountBtnRegister);

		registerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setControlsEnabled(false);
				performRegistration();
			}
		});

		signInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				performLogin();
			}
		});

		visitorButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setControlsEnabled(false);
				performAuthentication(null, null);
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

	private void performLogin() {
		final String username = emailEditor.getText().toString();
		startActivity(LoginActivity.createIntent(this, username, true, authenticatorResponse));
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

	private class TheTabChangeListener implements TabHost.OnTabChangeListener {
		private final InfoWebView infoWebView;
		private final Map<String, InfoPage> webView = new HashMap<>();

		public TheTabChangeListener(InfoWebView infoWebView) {
			this.infoWebView = infoWebView;
		}

		@Override
		public void onTabChanged(final String tag) {
			InfoPage page = webView.get(tag);
			if (page == null) {
				getRequestManager().loadInfoPage(tag, new DataRequestManager.DataResponse<InfoPage>() {
					@Override
					public void onSuccess(InfoPage data) {
						webView.put(tag, data);

						if (tag.equals(tabHost.getCurrentTabTag())) {
							infoWebView.showInfoPage(data);
							infoWebView.setVisibility(View.VISIBLE);
						}
					}

					@Override
					public void onFailure(String code, String message) {
					}

					@Override
					public void onDataError() {
					}

					@Override
					public void onConnectionError(int code) {
					}
				});
			} else {
				infoWebView.showInfoPage(page);
				infoWebView.setVisibility(View.VISIBLE);
			}
		}
	}
}
