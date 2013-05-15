package wisematches.client.android;

import android.app.Application;
import org.apache.http.HttpHost;
import wisematches.client.android.security.SecurityContext;
import wisematches.client.android.data.DataRequestManager;
import wisematches.client.android.data.service.JSONRequestManager;
import wisematches.client.android.graphics.BitmapFactory;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WiseMatchesApplication extends Application {
	private BitmapFactory bitmapFactory;

	private SecurityContext securityContext;
	private DataRequestManager requestManager;

	//	public static final HttpHost WEB_HOST = new HttpHost("www.wisematches.net", 80, "http");
	public static final HttpHost WEB_HOST = new HttpHost("10.139.202.145", 8080, "http");

	public WiseMatchesApplication() {
	}

	@Override
	public void onCreate() {
		super.onCreate();

		this.bitmapFactory = new BitmapFactory(getResources());
		this.securityContext = new SecurityContext();
		this.requestManager = new JSONRequestManager(getApplicationContext());
	}

	@Override
	public void onTerminate() {
		super.onTerminate();

		this.requestManager = null;

		this.securityContext.clear();
		this.securityContext = null;

		bitmapFactory.terminate();
	}

	public BitmapFactory getBitmapFactory() {
		return bitmapFactory;
	}

	public SecurityContext getSecurityContext() {
		return securityContext;
	}

	public DataRequestManager getRequestManager() {
		return requestManager;
	}
}