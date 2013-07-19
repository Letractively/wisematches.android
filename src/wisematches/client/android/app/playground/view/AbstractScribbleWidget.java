package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import wisematches.client.android.R;
import wisematches.client.android.data.model.scribble.ScribbleWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractScribbleWidget extends FrameLayout implements ScribbleWidget {
	private final String title;
	private final boolean showTitle;

	private final List<WidgetActionListener> listeners = new ArrayList<>();

	public AbstractScribbleWidget(Context context, AttributeSet attrs, int resource, String title) {
		this(context, attrs, resource, title, true);
	}

	public AbstractScribbleWidget(Context context, AttributeSet attrs, int resource, String title, boolean showTitle) {
		super(context, attrs);
		this.title = title;
		this.showTitle = showTitle;

		initView(context, resource, title, showTitle);
	}

	protected void initView(Context context, int resource, String title, boolean showTitle) {
		if (showTitle) {
			inflate(context, R.layout.playground_board_widget, this);

			final TextView titleFld = (TextView) findViewById(R.id.playgroundBoardWidgetTitle);
			titleFld.setText(title);

			inflate(context, resource, (ViewGroup) findViewById(R.id.playgroundBoardWidgetContent));
		} else {
			inflate(context, resource, this);
		}
	}

	public void addWidgetActionListener(WidgetActionListener l) {
		listeners.add(l);
	}

	public void removeWidgetActionListener(WidgetActionListener l) {
		listeners.remove(l);
	}

	public String getTitle() {
		return title;
	}

	public boolean isShowTitle() {
		return showTitle;
	}

	protected void notifyWidgetActionDone() {
		for (WidgetActionListener listener : listeners) {
			listener.onWidgetActionDone();
		}
	}
}
