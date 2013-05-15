package wisematches.client.android;

import android.content.Intent;
import android.net.Uri;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import org.apache.http.HttpHost;
import wisematches.client.android.security.SecurityContext;
import wisematches.client.android.data.DataRequestManager;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.graphics.BitmapFactory;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class WiseMatchesActivity extends SherlockFragmentActivity {
	public WiseMatchesActivity() {
	}

	protected Personality getPersonality() {
		return getSecurityContext().getPersonality();
	}

	protected SecurityContext getSecurityContext() {
		return ((WiseMatchesApplication) getApplication()).getSecurityContext();
	}

	protected DataRequestManager getRequestManager() {
		return ((WiseMatchesApplication) getApplication()).getRequestManager();
	}

	protected BitmapFactory getBitmapFactory() {
		return ((WiseMatchesApplication) getApplication()).getBitmapFactory();
	}

	@Deprecated
	protected void openPage(String uri) {
		final HttpHost host = WiseMatchesApplication.WEB_HOST;
		final String hostName = host.getHostName() + (host.getPort() == -1 ? "" : ":" + host.getPort());
		final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + hostName + "/" + uri));
		getApplicationContext().startActivity(Intent.createChooser(intent, "Chose browser"));
	}
}
