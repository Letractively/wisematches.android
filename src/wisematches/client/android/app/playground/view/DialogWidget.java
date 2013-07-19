package wisematches.client.android.app.playground.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ScrollView;
import com.actionbarsherlock.app.SherlockDialogFragment;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DialogWidget extends SherlockDialogFragment {
	private final AbstractScribbleWidget widget;

	public DialogWidget(AbstractScribbleWidget widget) {
		this.widget = widget;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setCancelable(true);
		setUserVisibleHint(true);

		final Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.setCanceledOnTouchOutside(true);

		dialog.setTitle(widget.getTitle());

		final ScrollView scrollView = new ScrollView(getActivity().getBaseContext(), null);
		scrollView.addView(widget);
		dialog.setContentView(scrollView);

		widget.addWidgetActionListener(new WidgetActionListener() {
			@Override
			public void onWidgetActionDone() {
				dismiss();
			}
		});
		return dialog;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		ViewGroup group = (ViewGroup) widget.getParent();
		if (group != null) {
			group.removeView(widget);
		}
		super.onDismiss(dialog);
	}
}
