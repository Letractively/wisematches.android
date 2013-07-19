package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import wisematches.client.android.R;
import wisematches.client.android.data.model.scribble.ScribbleBoard;
import wisematches.client.android.data.model.scribble.ScribbleTile;
import wisematches.client.android.data.model.scribble.ScribbleWord;
import wisematches.client.android.data.model.scribble.SelectionListener;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ControlsWidget extends AbstractScribbleWidget {
	private Button makeTurn;
	private Button passTurn;
	private Button exchangeTiles;

	private Button clearSelection;

	private ScribbleBoard scribbleBoard;

	private final TheSelectionListener selectionListener = new TheSelectionListener();

	public ControlsWidget(Context context, AttributeSet attrs) {
		super(context, attrs, R.layout.playground_board_widget_controls, null, false);

		makeTurn = (Button) findViewById(R.id.scribbleBoardBtnMake);
		makeTurn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				scribbleBoard.makeTurn(scribbleBoard.getSelectionModel().getSelectedWord());
			}
		});

		passTurn = (Button) findViewById(R.id.scribbleBoardBtnPass);
		passTurn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				scribbleBoard.passTurn();
			}
		});

		exchangeTiles = (Button) findViewById(R.id.scribbleBoardBtnExchange);
		exchangeTiles.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO: not implemented
			}
		});

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
			if (tiles != null && tiles.length != 0) {
				clearSelection.setEnabled(true);
			} else {
				clearSelection.setEnabled(false);
			}

			final boolean viewerTurn = scribbleBoard.isViewerTurn();
			makeTurn.setEnabled(viewerTurn && word != null);
			passTurn.setEnabled(viewerTurn);
			exchangeTiles.setEnabled(viewerTurn);
		}
	}
}
