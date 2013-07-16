package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.util.AttributeSet;
import wisematches.client.android.R;
import wisematches.client.android.data.model.scribble.BoardStateListener;
import wisematches.client.android.data.model.scribble.ScribbleBoard;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ProgressWidget extends AbstractBoardWidget {
	private ProgressView progressView;
	private ScribbleBoard scribbleBoard;

	private final TheBoardStateListener boardStateListener = new TheBoardStateListener();

	public ProgressWidget(Context context, AttributeSet attrs) {
		super(context, attrs, R.layout.playground_board_widget_progress, "Прогресс");

		progressView = (ProgressView) findViewById(R.id.scribbleBoardProgressView);
	}

	@Override
	public void boardInitialized(ScribbleBoard board) {
		this.scribbleBoard = board;
		board.addBoardStateListener(boardStateListener);

		progressView.updateProgress(scribbleBoard);
	}

	@Override
	public void boardTerminated(ScribbleBoard board) {
		board.removeBoardStateListener(boardStateListener);
	}

	private class TheBoardStateListener implements BoardStateListener {
		@Override
		public void gameStateChanged() {
			progressView.updateProgress(scribbleBoard);
		}

		@Override
		public void gameStateValidated() {
		}
	}
}
