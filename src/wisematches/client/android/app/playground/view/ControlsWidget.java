package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import wisematches.client.android.R;
import wisematches.client.android.data.model.scribble.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ControlsWidget extends FrameLayout implements ScribbleWidget {
	private Button make;
	private Button pass;
	private Button exchange;

	private Button clearSelection;

	private ScribbleBoard scribbleBoard;

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
				scribbleBoard.getSelectionModel().clearSelection();
			}
		});
	}

	@Override
	public void boardInitialized(ScribbleBoard board) {
		this.scribbleBoard = board;
		board.addSelectionListener(selectionListener);
	}

	@Override
	public void boardTerminated(ScribbleBoard board) {
		board.removeSelectionListener(selectionListener);
		this.scribbleBoard = null;
	}

	private class TheSelectionListener implements SelectionListener {
		@Override
		public void onSelectionChanged(ScribbleWord word, ScribbleTile[] tiles) {
			if (word != null) {
				clearSelection.setEnabled(true);
			} else {
				clearSelection.setEnabled(false);
			}
		}
	}
}
