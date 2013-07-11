package wisematches.client.android.app.playground.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.ActionBar;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesActivity;
import wisematches.client.android.app.playground.model.ScribbleController;
import wisematches.client.android.app.playground.model.ScribbleWidget;
import wisematches.client.android.app.playground.model.SelectionListener;
import wisematches.client.android.data.DataRequestManager;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.model.scribble.*;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameControllerActivity extends WiseMatchesActivity implements ScribbleController {
	private ScribbleBoard board;
	private DataRequestManager requestManager;

	private final List<ScribbleMove> scribbleMoves = new ArrayList<>();

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
			requestManager.openBoard(boardId, new SmartDataResponse<ScribbleBoard>(this) {
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

		scribbleMoves.addAll(board.getMoves());

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
	public void resign() {
		requestManager.resignGame(board.getId(), new SmartDataResponse<ScribbleChanges>(getBaseContext()) {
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
	public ScoreEngine getScoreEngine() {
		return board.getScoreEngine();
	}

	@Override
	public ScribbleSettings getSettings() {
		return board.getSettings();
	}

	@Override
	public Personality getPlayerTurn() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public List<Personality> getPlayers() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public Personality getPlayer(long player) {
		return board.getPlayer(player).getPlayer();
	}

	@Override
	public ScribbleTile[] getHandTiles() {
		return board.getHandTiles();
	}

	@Override
	public List<ScribbleMove> getScribbleMoves() {
		return scribbleMoves;
	}

	@Override
	public ScribbleWord getSelectedWord() {
		return null;
	}

	@Override
	public void selectWord(ScribbleWord word) {
		ScoreCalculation calculation = null;
		Collection<ScribbleTile> tiles = null;
		if (word != null) {
			calculation = board.getScoreEngine().calculateWordScore(board, word);
			tiles = Arrays.asList(word.getTiles());
		}

		for (SelectionListener selectionListener : selectionListeners) {
			selectionListener.onSelectionChanged(word, calculation, tiles);
		}
	}

	@Override
	public Set<ScribbleTile> getSelectedTiles() {
		return null;
	}

	@Override
	public void clearSelection() {
	}

	public static Intent createIntent(Context context, long boardId) {
		final Intent intent = new Intent(context, GameControllerActivity.class);
		intent.putExtra(INTENT_EXTRA_BOARD_ID, boardId);
		return intent;
	}
}