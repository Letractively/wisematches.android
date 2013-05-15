package wisematches.client.android.app.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesActivity;
import wisematches.client.android.data.DataRequestManager;
import wisematches.client.android.data.model.person.Personality;

import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RegisterActivity extends WiseMatchesActivity {
	private EditText emailEditor;
	private EditText nicknameEditor;
	private Spinner languageEditor;
	private EditText passwordEditor;
	private EditText confirmationEditor;

	private TextView errorField;

	private static final String INTENT_EXTRA_EMAIL = "ACCOUNT_EMAIL";
	private static final String INTENT_EXTRA_NICKNAME = "ACCOUNT_NICKNAME";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_register);

		emailEditor = (EditText) findViewById(R.id.accountFldEmail);
		nicknameEditor = (EditText) findViewById(R.id.accountFldNickname);
		languageEditor = (Spinner) findViewById(R.id.accountFldLanguage);
		passwordEditor = (EditText) findViewById(R.id.accountFldPwd);
		confirmationEditor = (EditText) findViewById(R.id.accountFldPwdCnf);

		errorField = (TextView) findViewById(R.id.accountFldError);

		final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages_array_values, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		((Spinner) findViewById(R.id.accountFldLanguage)).setAdapter(adapter);

		final Intent intent = getIntent();

		final String email = intent.getStringExtra(INTENT_EXTRA_EMAIL);
		if (email != null) {
			emailEditor.setText(email);
		}

		final String nickname = intent.getStringExtra(INTENT_EXTRA_NICKNAME);
		if (nickname != null) {
			nicknameEditor.setText(nickname);
		}

		findViewById(R.id.accountBtnRegister).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				performRegistration();
			}
		});
	}

	private void performRegistration() {
		CharSequence[] textArray = getResources().getTextArray(R.array.languages_array_codes);
		CharSequence lang = textArray[languageEditor.getSelectedItemPosition()];

		getRequestManager().register(
				nicknameEditor.getText().toString(),
				emailEditor.getText().toString(),
				passwordEditor.getText().toString(),
				confirmationEditor.getText().toString(),
				lang.toString(),
				TimeZone.getDefault().getID(),
				new DataRequestManager.DataResponse<Personality>() {
					@Override
					public void onSuccess(Personality data) {
						final Intent res = new Intent();
						res.putExtra("PERSONALITY", data);
						setResult(1, res);
					}

					@Override
					public void onFailure(String code, String message) {
						setErrorMessage(message);
					}

					@Override
					public void onDataError() {
						setErrorMessage("Ошибка при работе с сервером");
					}

					@Override
					public void onConnectionError(int code) {
						setErrorMessage("Ошибка при работе с сервером: " + code);
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

	public static Intent createIntent(Context context, String email, String nickname) {
		final Intent intent = new Intent(context, RegisterActivity.class);
		if (email != null) {
			intent.putExtra(INTENT_EXTRA_EMAIL, email);
		}
		if (nickname != null) {
			intent.putExtra(INTENT_EXTRA_NICKNAME, nickname);
		}
		return intent;
	}
}
