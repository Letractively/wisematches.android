package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import wisematches.client.android.R;
import wisematches.client.android.app.playground.model.ScribbleWidget;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class ScribbleWidgetView extends FrameLayout implements ScribbleWidget {
	public ScribbleWidgetView(Context context, AttributeSet attrs, int resource, String title) {
		super(context, attrs);

		initView(context, resource, title);
	}

	protected void initView(Context context, int resource, String title) {
		inflate(context, R.layout.playground_board_widget, this);

		final TextView titleFld = (TextView) findViewById(R.id.playgroundBoardWidgetTitle);
		titleFld.setText(title);

		inflate(context, resource, (ViewGroup) findViewById(R.id.playgroundBoardWidgetContent));
	}
}
