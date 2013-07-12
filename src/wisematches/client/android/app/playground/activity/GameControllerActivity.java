package wisematches.client.android.app.playground.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.ActionBar;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesActivity;
import wisematches.client.android.app.playground.model.GameMoveListener;
import wisematches.client.android.app.playground.model.ScribbleController;
import wisematches.client.android.app.playground.model.ScribbleWidget;
import wisematches.client.android.app.playground.model.SelectionListener;
import wisematches.client.android.data.DataRequestManager;
import wisematches.client.android.data.model.scribble.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameControllerActivity extends WiseMatchesActivity implements ScribbleController {
	private ScribbleBoard board;
	private DataRequestManager requestManager;

	private final List<GameMoveListener> gameMoveListeners = new ArrayList<>();
	private final List<SelectionListener> selectionListeners = new ArrayList<>();

	private static final String INTENT_EXTRA_BOARD_ID = "INTENT_EXTRA_BOARD_ID";

	public GameControllerActivity() {
		super(null, R.layout.playground_board, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestManager = getRequestManager();

		final long boardId = getIntent().getLongExtra(INTENT_EXTRA_BOARD_ID, 0);
		if (boardId == 0) {
			finish();
		} else {
			requestManager.openBoard(boardId, new SmartDataResponse<ScribbleBoard>() {
				@Override
				protected void onData(ScribbleBoard board) {
					initializeController(board);
				}

				@Override
				protected void onRetry() {
					requestManager.openBoard(boardId, this);
				}

				@Override
				protected void onCancel() {
				}
			});
		}
	}

	private void initializeController(ScribbleBoard board) {
		this.board = board;

		final ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setTitle(board.getSettings().getTitle() + " #" + board.getId());
		}

		final Set<ScribbleWidget> widgets = new HashSet<>();
		findScribbleWidgets(getWindow().getDecorView(), widgets);

		for (ScribbleWidget widget : widgets) {
			widget.controllerInitialized(this);
		}
	}

	private void findScribbleWidgets(View view, Set<ScribbleWidget> widgets) {
		if (view instanceof ScribbleWidget) {
			widgets.add((ScribbleWidget) view);
		}
		if (view instanceof ViewGroup) {
			final ViewGroup vg = (ViewGroup) view;

			final int childCount = vg.getChildCount();
			for (int i = 0; i < childCount; i++) {
				findScribbleWidgets(vg.getChildAt(i), widgets);
			}
		}
	}

	@Override
	public void addSelectionListener(SelectionListener l) {
		if (l != null) {
			selectionListeners.add(l);
		}
	}

	@Override
	public void removeSelectionListener(SelectionListener l) {
		if (l != null) {
			selectionListeners.remove(l);
		}
	}

	@Override
	public void addGameMoveListener(GameMoveListener l) {
		if (l != null) {
			gameMoveListeners.add(l);
		}
	}

	@Override
	public void removeGameMoveListener(GameMoveListener l) {
		if (l != null) {
			gameMoveListeners.remove(l);
		}
	}

	@Override
	public void resign() {
		requestManager.resignGame(board.getId(), new SmartDataResponse<ScribbleChanges>() {
			@Override
			protected void onData(ScribbleChanges data) {
			}

			@Override
			protected void onRetry() {
				requestManager.resignGame(board.getId(), this);
			}

			@Override
			protected void onCancel() {
			}
		});
	}

	@Override
	public void passTurn() {
	}

	@Override
	public void makeTurn(ScribbleWord word) {
	}

	@Override
	public void exchange(Set<ScribbleTile> tiles) {
	}


	@Override
	public ScribbleWord getSelectedWord() {
		return null;
	}

	@Override
	public void selectWord(ScribbleWord word) {
		ScoreCalculation calculation = null;
		if (word != null) {
			calculation = board.getScoreEngine().calculateWordScore(board, word);
		}

		for (SelectionListener selectionListener : selectionListeners) {
			selectionListener.onSelectionChanged(word, calculation);
		}
	}

	@Override
	public ScribbleBoard getScribbleBoard() {
		return board;
	}

	public static Intent createIntent(Context context, long boardId) {
		final Intent intent = new Intent(context, GameControllerActivity.class);
		intent.putExtra(INTENT_EXTRA_BOARD_ID, boardId);
		return intent;
	}
}