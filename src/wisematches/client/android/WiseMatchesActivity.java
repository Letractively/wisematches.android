package wisematches.client.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import wisematches.client.android.app.MenuFactory;
import wisematches.client.android.app.account.LoginActivity;
import wisematches.client.android.data.DataRequestManager;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.graphics.BitmapFactory;
import wisematches.client.android.security.SecurityContext;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class WiseMatchesActivity extends SherlockActivity {
	private final String title;
	private final int viewId;
	private final boolean waitBeforeView;

	private View progressBar;

	public WiseMatchesActivity(String title, int viewId) {
		this(title, viewId, false);
	}

	protected WiseMatchesActivity(String title, int viewId, boolean waitBeforeView) {
		this.title = title;
		this.viewId = viewId;
		this.waitBeforeView = waitBeforeView;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final LayoutInflater layoutInflater = getLayoutInflater();

		final LinearLayout rootView = new LinearLayout(this);
		rootView.setOrientation(LinearLayout.VERTICAL);

		layoutInflater.inflate(R.layout.progress_loading, rootView, true);
		layoutInflater.inflate(viewId, rootView, true);

		setContentView(rootView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		progressBar = findViewById(R.id.commonLoadingProgress);

		if (waitBeforeView) {
			showWaitingView();
		}

		final ActionBar actionBar = getSupportActionBar();
		if (actionBar != null && title != null) {
			actionBar.setTitle(title);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuFactory.startMenuActivity(this, item);
	}

	protected Personality getPersonality(boolean safe) {
		Personality personality = getSecurityContext().getPersonality();
		if (safe && personality == null) {
			startActivity(LoginActivity.createIntent(this, null, true, null));
		}
		return personality;
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

	protected void showWaitingView() {
//		final View progressBar = findViewById(R.id.commonLoadingProgress);
		progressBar.setVisibility(View.VISIBLE);
	}

	protected void hideWaitingView() {
//		final View progressBar = findViewById(R.id.commonLoadingProgress);
		progressBar.setVisibility(View.GONE);
	}

	protected abstract class SmartDataResponse<T> implements DataRequestManager.DataResponse<T> {
		private final Context context;

		protected SmartDataResponse(Context context) {
			this.context = context;
		}

		protected abstract void onData(T data);

		protected abstract void onRetry();

		protected abstract void onCancel();

		@Override
		public void onSuccess(T data) {
			onData(data);
			hideWaitingView();
		}

		@Override
		public void onDataError() {
			showErrorDialog("Ответ от сервера не может быть обработан.", true);
		}

		@Override
		public void onConnectionError(int code) {
			showErrorDialog("Сервер не отвечает либо не может обработать запрос.", true);
		}

		@Override
		public void onFailure(String code, String message) {
			showErrorDialog(message, false);
			hideWaitingView();
		}

		protected void showErrorDialog(String message, boolean retry) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(Html.fromHtml(message));
			if (retry) {
				builder.setPositiveButton("Повторить", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						onRetry();
						dialog.dismiss();
					}
				});
				builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						onCancel();
						dialog.dismiss();
						hideWaitingView();
					}
				});
			} else {
				builder.setNeutralButton("Закрыть", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						onCancel();
						dialog.dismiss();
						hideWaitingView();
					}
				});
			}
			builder.show();
		}
	}
}
