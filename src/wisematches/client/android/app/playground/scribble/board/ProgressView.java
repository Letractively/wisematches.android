package wisematches.client.android.app.playground.scribble.board;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import wisematches.client.android.app.playground.scribble.board.surface.ProgressSurface;
import wisematches.client.android.data.model.scribble.ScribbleBank;
import wisematches.client.android.data.model.scribble.ScribbleBoard;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ProgressView extends View {
	private ProgressSurface progressSurface;

	public ProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);

		if (!isInEditMode()) {
			progressSurface = new ProgressSurface(context.getResources());
		}
	}

	protected void onDraw(Canvas canvas) {
		if (progressSurface != null) {
			progressSurface.onDraw(canvas, getWidth(), getHeight());
		}
	}

	public void updateProgress(ScribbleBoard board) {
		if (progressSurface == null) {
			return;
		}

		if (board.isActive()) {
			final ScribbleBank bank = board.getScribbleBank();

			final int totalTiles = bank.getLettersCount();
			final int boardTiles = board.getBoardTilesCount();

			int k = board.getPlayers().length * 7;
			final int handTiles = (k <= totalTiles - boardTiles) ? k : totalTiles - boardTiles;

			progressSurface.updateProgress(boardTiles, handTiles, totalTiles);
		} else {
			progressSurface.finalizeProgress("Finished");
		}
	}
}