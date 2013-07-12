package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import wisematches.client.android.R;
import wisematches.client.android.app.playground.model.ScribbleController;
import wisematches.client.android.app.playground.model.SelectionListener;
import wisematches.client.android.data.model.scribble.ScoreCalculation;
import wisematches.client.android.data.model.scribble.ScribbleWord;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SelectedWordWidget extends AbstractBoardWidget {
	private TextView pointsCalculationFld;
	private SelectedWordView selectedTilesView;

	private final TheSelectionListener selectionListener = new TheSelectionListener();

	public SelectedWordWidget(Context context, AttributeSet attrs) {
		super(context, attrs, R.layout.playground_board_widget_selection, "Выделенное слово");

		selectedTilesView = (SelectedWordView) findViewById(R.id.scribbleBoardFltTiles);
		pointsCalculationFld = (TextView) findViewById(R.id.scribbleBoardFltPoints);
	}

	@Override
	public void controllerInitialized(ScribbleController controller) {
		controller.addSelectionListener(selectionListener);
	}

	@Override
	public void controllerTerminated(ScribbleController controller) {
		controller.removeSelectionListener(selectionListener);
	}

	private class TheSelectionListener implements SelectionListener {
		@Override
		public void onSelectionChanged(ScribbleWord word, ScoreCalculation score) {
			if (score != null) {
				selectedTilesView.setScribbleTiles(word.getTiles());
				pointsCalculationFld.setText(score.getFormula() + "=" + score.getPoints());
			} else {
				selectedTilesView.setScribbleTiles(null);
				pointsCalculationFld.setText("Составьте слово");
			}
		}
	}
}
