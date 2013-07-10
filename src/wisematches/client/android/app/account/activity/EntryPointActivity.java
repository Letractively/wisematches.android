package wisematches.client.android.app.account.activity;

import android.os.Bundle;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesActivity;
import wisematches.client.android.app.playground.activity.ActiveGamesActivity;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.security.SecurityContext;

public class EntryPointActivity extends WiseMatchesActivity {
	public EntryPointActivity() {
		super(null, R.layout.splash);
	}

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final SecurityContext.AuthorizationListener authorizationListener = new SecurityContext.AuthorizationListener() {
			@Override
			public void authorized(Personality personality) {
				final EntryPointActivity activity = EntryPointActivity.this;
				if (personality == null) {
					getSecurityContext().authenticate(activity, this);
				} else {
					activity.startActivity(ActiveGamesActivity.createIntent(activity, personality.getId()));
				}
			}
		};
		getSecurityContext().authenticate(this, authorizationListener);
	}
}
