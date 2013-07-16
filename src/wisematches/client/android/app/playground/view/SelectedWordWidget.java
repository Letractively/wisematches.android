package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import wisematches.client.android.R;
import wisematches.client.android.data.model.scribble.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SelectedWordWidget extends AbstractBoardWidget {
	private TextView pointsCalculationFld;
	private SelectedWordView selectedTilesView;

	private final TheSelectionListener selectionListener = new TheSelectionListener();
	private ScribbleBoard board;

	public SelectedWordWidget(Context context, AttributeSet attrs) {
		super(context, attrs, R.layout.playground_board_widget_selection, "Выделенное слово");

		selectedTilesView = (SelectedWordView) findViewById(R.id.scribbleBoardFltTiles);
		pointsCalculationFld = (TextView) findViewById(R.id.scribbleBoardFltPoints);
	}

	@Override
	public void boardInitialized(ScribbleBoard board) {
		this.board = board;
		board.addSelectionListener(selectionListener);
	}

	@Override
	public void boardTerminated(ScribbleBoard board) {
		board.removeSelectionListener(selectionListener);
	}

	private class TheSelectionListener implements SelectionListener {
		@Override
		public void onSelectionChanged(ScribbleWord word, ScribbleTile[] tiles) {
			if (word != null) {
				final ScoreCalculation score = board.calculateScore(word);
				selectedTilesView.setScribbleTiles(word.getTiles());
				pointsCalculationFld.setText(score.getFormula() + "=" + score.getPoints());
			} else {
				pointsCalculationFld.setText("");
				selectedTilesView.setScribbleTiles(null);
			}
		}
	}
}
