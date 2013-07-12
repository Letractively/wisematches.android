package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.util.AttributeSet;
import wisematches.client.android.R;
import wisematches.client.android.app.playground.model.ScribbleController;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ProgressWidget extends AbstractBoardWidget {
	private ProgressView progressView;

	public ProgressWidget(Context context, AttributeSet attrs) {
		super(context, attrs, R.layout.playground_board_widget_progress, "Прогресс");

		progressView = (ProgressView) findViewById(R.id.scribbleBoardProgressView);
	}

	@Override
	public void controllerInitialized(ScribbleController controller) {
		progressView.updateProgress(controller);
	}

	@Override
	public void controllerTerminated(ScribbleController controller) {
	}
}
