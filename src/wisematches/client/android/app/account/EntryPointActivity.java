package wisematches.client.android.app.account;

import android.os.Bundle;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesActivity;
import wisematches.client.android.app.playground.scribble.ActiveGamesActivity;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.security.SecurityContext;

public class EntryPointActivity extends WiseMatchesActivity {
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

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
