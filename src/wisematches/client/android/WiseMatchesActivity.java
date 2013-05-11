package wisematches.client.android;

import com.actionbarsherlock.app.SherlockActivity;
import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager;
import wisematches.client.android.app.account.model.Player;
import wisematches.client.android.graphics.BitmapFactory;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class WiseMatchesActivity extends SherlockActivity {
	public WiseMatchesActivity() {
	}

	protected final void execute(Request request, RequestManager.RequestListener listener) {
		getApp().getRequestManager().execute(request, listener);
	}

	@Deprecated
	public Player getPrincipal() {
		return getApp().getPrincipal();
	}

	@Deprecated
	public BitmapFactory getBitmapFactory() {
		return getApp().getBitmapFactory();
	}

	@Deprecated
	private WiseMatchesApplication getApp() {
		return (WiseMatchesApplication) getApplication();
	}
}
