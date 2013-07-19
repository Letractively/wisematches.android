package wisematches.client.android.app.playground.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesActivity;
import wisematches.client.android.app.playground.MenuFactory;
import wisematches.client.android.app.playground.view.*;
import wisematches.client.android.data.DataRequestManager;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.model.scribble.*;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameControllerActivity extends WiseMatchesActivity implements ScribbleController {
	private ScribbleBoard board;
	private DataRequestManager requestManager;

	private MovesWidget movesWidget;
	private PlayerWidget playerWidget;
	private ProgressWidget progressWidget;
	private ControlsWidget controlsWidget;
	private PlaygroundWidget playgroundWidget;
	private DictionaryWidget dictionaryWidget;
	private SelectedWordWidget selectedWordWidget;

	private DialogWidget movesHistoryDialog;

	private final Set<MenuItem> widgetsMenuItems = new HashSet<>();

	private Timer timer;

	private static final String INTENT_EXTRA_BOARD_ID = "INTENT_EXTRA_BOARD_ID";

	public GameControllerActivity() {
		super(null, R.layout.playground_board, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		movesWidget = findViewByType(MovesWidget.class);
		if (movesWidget == null) {
			movesWidget = new MovesWidget(getBaseContext(), null, false);
			movesHistoryDialog = new DialogWidget(movesWidget);
		}

		playerWidget = findViewByType(PlayerWidget.class);
		controlsWidget = findViewByType(ControlsWidget.class);
		progressWidget = findViewByType(ProgressWidget.class);
		playgroundWidget = findViewByType(PlaygroundWidget.class);
		dictionaryWidget = findViewByType(DictionaryWidget.class);
		selectedWordWidget = findViewByType(SelectedWordWidget.class);

		requestManager = getRequestManager();
		final long boardId = getIntent().getLongExtra(INTENT_EXTRA_BOARD_ID, 0);
		if (boardId == 0) {
			finish();
		} else {
			requestManager.openBoard(boardId, new SmartDataResponse<ScribbleSnapshot>() {
				@Override
				protected void onData(ScribbleSnapshot board) {
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

	@Override
	protected void onDestroy() {
		destroyController();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (movesHistoryDialog != null) {
			widgetsMenuItems.add(MenuFactory.addMenuItem(menu, 1, 1, MenuFactory.Type.MOVES_HISTORY, MenuFactory.Visibility.ALWAYS));
		}
		for (MenuItem widgetsMenuItem : widgetsMenuItems) {
			widgetsMenuItem.setEnabled(false);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (MenuFactory.Type.MOVES_HISTORY.is(item)) {
			movesHistoryDialog.show(getSupportFragmentManager(), "hist");
		}
		return true;
	}

	private void initializeController(ScribbleSnapshot snapshot) {
		this.board = new ScribbleBoard(this, snapshot);

		final ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setTitle(board.getSettings().getTitle() + " #" + board.getId());
		}

		movesWidget.boardInitialized(board);
		playerWidget.boardInitialized(board);
		controlsWidget.boardInitialized(board);
		progressWidget.boardInitialized(board);
		playgroundWidget.boardInitialized(board);
		dictionaryWidget.boardInitialized(board);
		selectedWordWidget.boardInitialized(board);

		for (MenuItem widgetsMenuItem : widgetsMenuItems) {
			widgetsMenuItem.setEnabled(true);
		}

		if (snapshot.getDescriptor().getStatus().isActive()) {
			timer = new Timer("BoardValidationTime");
			timer.schedule(new BoardValidationTask(), 0, 1000);
		}
	}

	private void destroyController() {
		if (timer != null) {
			timer.cancel();
		}

		for (MenuItem widgetsMenuItem : widgetsMenuItems) {
			widgetsMenuItem.setEnabled(false);
		}

		movesWidget.boardTerminated(board);
		playerWidget.boardTerminated(board);
		controlsWidget.boardTerminated(board);
		progressWidget.boardTerminated(board);
		playgroundWidget.boardTerminated(board);
		dictionaryWidget.boardTerminated(board);
		selectedWordWidget.boardTerminated(board);

		this.board = null;
	}

	private <T extends ScribbleWidget> T findViewByType(Class<T> type) {
		return findViewByType(getWindow().getDecorView(), type);
	}

	@SuppressWarnings("unchecked")
	private <T extends ScribbleWidget> T findViewByType(View view, Class<T> type) {
		if (type.isAssignableFrom(view.getClass())) {
			return (T) view;
		}
		if (view instanceof ViewGroup) {
			final ViewGroup vg = (ViewGroup) view;

			final int childCount = vg.getChildCount();
			for (int i = 0; i < childCount; i++) {
				final T viewByType = findViewByType(vg.getChildAt(i), type);
				if (viewByType != null) {
					return viewByType;
				}
			}
		}
		return null;
	}

	@Override
	public void resign(final BoardValidator validator) {
		requestManager.resignGame(board.getId(), new SmartDataResponse<ScribbleChanges>() {
			@Override
			protected void onData(ScribbleChanges data) {
				validator.validateBoard(data);
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
	public void passTurn(final BoardValidator validator) {
		requestManager.passTurn(board.getId(), new SmartDataResponse<ScribbleChanges>() {
			@Override
			protected void onData(ScribbleChanges data) {
				validator.validateBoard(data);
			}

			@Override
			protected void onRetry() {
				requestManager.passTurn(board.getId(), this);
			}

			@Override
			protected void onCancel() {
			}
		});
	}

	@Override
	public void makeTurn(final ScribbleWord word, final BoardValidator validator) {
		requestManager.makeTurn(board.getId(), word, new SmartDataResponse<ScribbleChanges>() {
			@Override
			protected void onData(ScribbleChanges data) {
				validator.validateBoard(data);
			}

			@Override
			protected void onRetry() {
				requestManager.makeTurn(board.getId(), word, this);
			}

			@Override
			protected void onCancel() {
			}
		});
	}

	@Override
	public void exchange(final Set<ScribbleTile> tiles, final BoardValidator validator) {
		requestManager.exchangeTiles(board.getId(), tiles, new SmartDataResponse<ScribbleChanges>() {
			@Override
			protected void onData(ScribbleChanges data) {
				validator.validateBoard(data);
			}

			@Override
			protected void onRetry() {
				requestManager.exchangeTiles(board.getId(), tiles, this);
			}

			@Override
			protected void onCancel() {
			}
		});
	}

	@Override
	public Personality getBoardViewer() {
		return getPersonality(false);
	}

	public static Intent createIntent(Context context, long boardId) {
		final Intent intent = new Intent(context, GameControllerActivity.class);
		intent.putExtra(INTENT_EXTRA_BOARD_ID, boardId);
		return intent;
	}

	private class BoardValidationTask extends TimerTask {
		@Override
		public void run() {
			requestManager.validateBoard(board.getId(), false, new DataRequestManager.DataResponse<ScribbleChanges>() {
				@Override
				public void onSuccess(ScribbleChanges data) {
					board.validateBoard(data);
				}

				@Override
				public void onFailure(String code, String message) {
				}

				@Override
				public void onDataError() {
				}

				@Override
				public void onConnectionError(int code) {
				}
			});
		}
	}
}