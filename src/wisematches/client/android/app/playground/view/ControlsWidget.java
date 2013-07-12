package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import wisematches.client.android.R;
import wisematches.client.android.app.playground.model.ScribbleController;
import wisematches.client.android.app.playground.model.ScribbleWidget;
import wisematches.client.android.app.playground.model.SelectionListener;
import wisematches.client.android.data.model.scribble.ScoreCalculation;
import wisematches.client.android.data.model.scribble.ScribbleWord;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ControlsWidget extends FrameLayout implements ScribbleWidget {
	private Button make;
	private Button pass;
	private Button exchange;

	private Button clearSelection;

	private ScribbleController controller;

	private final TheSelectionListener selectionListener = new TheSelectionListener();

	public ControlsWidget(Context context, AttributeSet attrs) {
		super(context, attrs);

		inflate(context, R.layout.playground_board_widget_controls, this);

		make = (Button) findViewById(R.id.scribbleBoardBtnMake);
		pass = (Button) findViewById(R.id.scribbleBoardBtnPass);
		exchange = (Button) findViewById(R.id.scribbleBoardBtnExchange);

		clearSelection = (Button) findViewById(R.id.scribbleBoardBtnDeselect);
		clearSelection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				controller.selectWord(null);
			}
		});
	}

	@Override
	public void controllerInitialized(ScribbleController controller) {
		this.controller = controller;
		controller.addSelectionListener(selectionListener);
	}

	@Override
	public void controllerTerminated(ScribbleController controller) {
		controller.removeSelectionListener(selectionListener);
		this.controller = null;
	}

	private class TheSelectionListener implements SelectionListener {
		@Override
		public void onSelectionChanged(ScribbleWord word, ScoreCalculation score) {
			if (word != null) {
				clearSelection.setEnabled(true);
			} else {
				clearSelection.setEnabled(false);
			}
		}
	}
}
