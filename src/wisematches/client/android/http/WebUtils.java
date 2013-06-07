package wisematches.client.android.http;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import org.apache.http.HttpHost;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class WebUtils {
	private WebUtils() {
	}

	public static void openPage(Context context, String uri) {
		final HttpHost host = WiseMatchesApplication.WEB_HOST;
		final String hostName = host.getHostName() + (host.getPort() == -1 ? "" : ":" + host.getPort());
		final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + hostName + "/" + uri));
		context.startActivity(Intent.createChooser(intent, "Chose browser"));
	}

	public static String getStyleSheets(Context context) {
		try {
			final InputStream is = context.getResources().openRawResource(R.raw.web_view_styles);
			final BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String readLine = null;
			final StringBuilder b = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				b.append(readLine).append("\n");
			}
			is.close();
			br.close();
			return b.toString();
		} catch (IOException ex) {
			return "";
		}
	}
}
