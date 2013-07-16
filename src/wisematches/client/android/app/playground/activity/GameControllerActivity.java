package wisematches.client.android.app.playground.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.ActionBar;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesActivity;
import wisematches.client.android.data.DataRequestManager;
import wisematches.client.android.data.model.scribble.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameControllerActivity extends WiseMatchesActivity implements ScribbleController {
	private ScribbleBoard board;
	private DataRequestManager requestManager;

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

	private void initializeController(ScribbleSnapshot snapshot) {
		this.board = new ScribbleBoard(this, snapshot);

		final ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setTitle(board.getSettings().getTitle() + " #" + board.getId());
		}

		final Set<ScribbleWidget> widgets = new HashSet<>();
		findScribbleWidgets(getWindow().getDecorView(), widgets);

		for (ScribbleWidget widget : widgets) {
			widget.boardInitialized(board);
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

	public static Intent createIntent(Context context, long boardId) {
		final Intent intent = new Intent(context, GameControllerActivity.class);
		intent.putExtra(INTENT_EXTRA_BOARD_ID, boardId);
		return intent;
	}
}