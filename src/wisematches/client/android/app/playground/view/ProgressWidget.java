package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.util.AttributeSet;
import wisematches.client.android.R;
import wisematches.client.android.app.playground.model.GameMoveListener;
import wisematches.client.android.app.playground.model.ScribbleController;
import wisematches.client.android.data.model.scribble.ScribbleMove;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ProgressWidget extends AbstractBoardWidget {
	private ProgressView progressView;
	private ScribbleController controller;

	private final TheGameMoveListener gameMoveListener = new TheGameMoveListener();

	public ProgressWidget(Context context, AttributeSet attrs) {
		super(context, attrs, R.layout.playground_board_widget_progress, "Прогресс");

		progressView = (ProgressView) findViewById(R.id.scribbleBoardProgressView);
	}

	@Override
	public void controllerInitialized(ScribbleController controller) {
		this.controller = controller;
		controller.addGameMoveListener(gameMoveListener);

		progressView.updateProgress(controller.getScribbleBoard());
	}

	@Override
	public void controllerTerminated(ScribbleController controller) {
		controller.removeGameMoveListener(gameMoveListener);
	}

	private class TheGameMoveListener implements GameMoveListener {
		@Override
		public void onMoveDone(ScribbleMove move) {
			progressView.updateProgress(controller.getScribbleBoard());
		}
	}
}
