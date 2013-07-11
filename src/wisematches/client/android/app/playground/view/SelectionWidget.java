package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import wisematches.client.android.R;
import wisematches.client.android.app.playground.model.ScribbleController;
import wisematches.client.android.app.playground.model.SelectionListener;
import wisematches.client.android.data.model.scribble.ScoreCalculation;
import wisematches.client.android.data.model.scribble.ScribbleTile;
import wisematches.client.android.data.model.scribble.ScribbleWord;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SelectionWidget extends ScribbleWidgetView {
	private TextView pointsCalculationFld;

	private final TheSelectionListener selectionListener = new TheSelectionListener();

	public SelectionWidget(Context context, AttributeSet attrs) {
		super(context, attrs, R.layout.playground_board_widget_selection, "Баллы за выделенное слово");

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
		public void onSelectionChanged(ScribbleWord word, ScoreCalculation score, Collection<ScribbleTile> tiles) {
			if (score != null) {
				pointsCalculationFld.setText(score.getPoints() + " = " + score.getFormula());
			} else {
				pointsCalculationFld.setText("Составьте слово");
			}
		}
	}
}
