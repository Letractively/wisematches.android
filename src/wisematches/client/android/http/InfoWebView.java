package wisematches.client.android.http;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class InfoWebView extends WebView {
	public InfoWebView(Context context) {
		super(context);
		init();
	}

	public InfoWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public InfoWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		if (!isInEditMode()) {
			final WebSettings settings = getSettings();
			settings.setSupportZoom(false);
			settings.setJavaScriptEnabled(true);
		}
		setBackgroundColor(0x00000000);
	}

	public void loadInfo(String uri) {
		loadDataWithBaseURL("http://" + WiseMatchesApplication.WEB_HOST.toHostString(), "<html>" +
				"<head><style>" + getStyleSheets() + "</style></head>" +
				"<body><div id='Content'></div></body>\n" +
				"<script type=\"application/javascript\">\n" +
				"    req = new XMLHttpRequest();\n" +
				"    req.onreadystatechange = function () {\n" +
				"        if (req.readyState == 4) {\n" +
				"            if (req.status == 200) {\n" +
				"                document.getElementById('Content').innerHTML = req.responseText;\n" +
				"            }\n" +
				"        }\n" +
				"    };\n" +
				"    req.open('GET', '" + uri + "?plain=true', true);\n" +
				"    req.send(null);\n" +
				"</script>\n" +
				"</html>", "text/html", "UTF-8", null);
	}

	private String getStyleSheets() {
		try {
			final InputStream is = this.getResources().openRawResource(R.raw.web_view_styles);
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
