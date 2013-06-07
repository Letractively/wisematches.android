package wisematches.client.android.http;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import wisematches.client.android.WiseMatchesApplication;
import wisematches.client.android.data.DataRequestManager;
import wisematches.client.android.data.model.info.InfoPage;

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
			settings.setJavaScriptEnabled(false);
		}
		setWebViewClient(new TheWebViewClient());
		setBackgroundColor(0x00000000);
	}

	public void showPage(String name, DataRequestManager requestManager) {
		requestManager.loadInfoPage(name, new DataRequestManager.DataResponse<InfoPage>() {
			@Override
			public void onSuccess(InfoPage data) {
				showInfoPage(data);
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
	}

	public void showInfoPage(InfoPage infoPage) {
		final String styleSheets = WebUtils.getStyleSheets(this.getContext());
		final String text = "<html><head><style>" + styleSheets + "</style></head><body>" + infoPage.getText() + "</body></html>";
		loadDataWithBaseURL("http://" + WiseMatchesApplication.WEB_HOST.toHostString(), text, "text/html", "UTF-8", null);
	}

	private class TheWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
			// Do something with the event here
			return true;
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// reject anything other
			return true;
		}
	}
}
