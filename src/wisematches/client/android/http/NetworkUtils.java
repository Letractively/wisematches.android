package wisematches.client.android.http;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import org.apache.http.HttpHost;
import wisematches.client.android.WiseMatchesApplication;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class NetworkUtils {
	private NetworkUtils() {
	}

	public static void openPage(Context context, String uri) {
		final HttpHost host = WiseMatchesApplication.WEB_HOST;
		final String hostName = host.getHostName() + (host.getPort() == -1 ? "" : ":" + host.getPort());
		final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + hostName + "/" + uri));
		context.startActivity(Intent.createChooser(intent, "Chose browser"));
	}
}
