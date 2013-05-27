package wisematches.client.android.app.playground.scribble.board;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesActivity;
import wisematches.client.android.app.account.view.PersonalityView;
import wisematches.client.android.data.model.scribble.*;

import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleBoardActivity extends WiseMatchesActivity {
	private Button makeTurnBtn;
	private Button passTurnBtn;
	private Button exchangeTilesBtn;

	private Button selectTilesBtn;
	private Button clearSelectionBtn;

	private EditText dictField;
	private BoardView boardView;
	private Button dictionaryBth;
	private ProgressView progressView;

	private TextView pointsCalculationFld;

	private static final String INTENT_EXTRA_BOARD_ID = "INTENT_EXTRA_BOARD_ID";

	public ScribbleBoardActivity() {
		super(null, R.layout.playground_board, true);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		makeTurnBtn = (Button) findViewById(R.id.scribbleBoardBtnMake);
		makeTurnBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				makeTurn();
			}
		});

		passTurnBtn = (Button) findViewById(R.id.scribbleBoardBtnPass);
		passTurnBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				passTurn();
			}
		});

		exchangeTilesBtn = (Button) findViewById(R.id.scribbleBoardBtnExchange);
		exchangeTilesBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				exchangeTiles();
			}
		});

		selectTilesBtn = (Button) findViewById(R.id.scribbleBoardBtnSelect);
		selectTilesBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectTiles();
			}
		});

		clearSelectionBtn = (Button) findViewById(R.id.scribbleBoardBtnDeselect);
		clearSelectionBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clearSelection();
			}
		});

		dictField = (EditText) findViewById(R.id.scribbleBoardDictField);
		boardView = (BoardView) findViewById(R.id.scribbleBoardView);
		progressView = (ProgressView) findViewById(R.id.scribbleBoardProgressView);
		dictionaryBth = (Button) findViewById(R.id.scribbleBoardBtnDict);
		pointsCalculationFld = (TextView) findViewById(R.id.scribbleBoardFltPoints);

		long longExtra = getIntent().getLongExtra(INTENT_EXTRA_BOARD_ID, 0);
		if (longExtra == 0) {
			finish();
		} else {
			openBoard(longExtra);
		}
	}

	private void openBoard(final long boardId) {
		getRequestManager().openBoard(boardId, new SmartDataResponse<ScribbleBoard>(this) {
			@Override
			protected void onData(ScribbleBoard data) {
				showBoardInfo(data);
			}

			@Override
			protected void onRetry() {
				openBoard(boardId);
			}

			@Override
			protected void onCancel() {
			}
		});
	}

	private void makeTurn() {
		ScribbleWord word = boardView.getSelectedWord();
		if (word != null) {
			getRequestManager().makeTurn(boardView.getBoard().getId(), word, new SmartDataResponse<ScribbleChanges>(this) {
				@Override
				protected void onData(ScribbleChanges data) {
					updateBoardState(data);
				}

				@Override
				protected void onRetry() {
					makeTurn();
				}

				@Override
				protected void onCancel() {
				}
			});
		}
	}

	private void passTurn() {
		getRequestManager().passTurn(boardView.getBoard().getId(), new SmartDataResponse<ScribbleChanges>(this) {
			@Override
			protected void onData(ScribbleChanges data) {
				updateBoardState(data);
			}

			@Override
			protected void onRetry() {
				makeTurn();
			}

			@Override
			protected void onCancel() {
			}
		});
	}

	private void exchangeTiles() {
	}

	private void resignGame() {
	}

	private void selectTiles() {
	}

	private void clearSelection() {
		boardView.clearSelection();
	}

	private void updateBoardState(ScribbleChanges data) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		return true;
	}

	private void showBoardInfo(final ScribbleBoard board) {
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(board.getSettings().getTitle() + " #" + board.getId());

		boardView.initBoardView(board, getBitmapFactory());
		boardView.requestFocus();

		progressView.updateProgress(board);


		int index = 0;
		final TableLayout tableLayout = (TableLayout) findViewById(R.id.scribbleBoardPlayers);
		for (ScribbleHand hand : board.getPlayers()) {
			final TableRow row = (TableRow) tableLayout.getChildAt(index);

			final PersonalityView pv = (PersonalityView) row.getChildAt(0);
			pv.setPersonality(hand.getPlayer());

			final TextView view = (TextView) row.getChildAt(1);
			view.setText(String.valueOf(hand.getScores().getPoints()));

			index++;
		}
/*
		final ListView playersView = (ListView) findViewById(R.id.scribbleBoardPlayersView);
		playersView.setDivider(null);
		playersView.setAdapter(new ScribblePlayerAdapter(this, board.getPlayers()));
*/

		boardView.setScribbleBoardListener(new BoardViewListener() {
			@Override
			public void onTileSelected(ScribbleTile tile, boolean selected, Set<ScribbleTile> selectedTiles) {
				clearSelectionBtn.setEnabled(!selectedTiles.isEmpty());
			}

			@Override
			public void onWordSelected(ScribbleWord word) {
				boolean enabled = word != null;

				makeTurnBtn.setEnabled(enabled);
				passTurnBtn.setEnabled(enabled);
				exchangeTilesBtn.setEnabled(enabled);

				if (!enabled) {
					dictField.setText("");
					pointsCalculationFld.setText("");
				} else {
					dictField.setText(word.getText());

					final ScoreCalculation calculation = board.getScoreEngine().calculateWordScore(board, word);
					pointsCalculationFld.setText(calculation.getFormula() + " = " + calculation.getPoints());
				}
			}
		});

		dictionaryBth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
	}

	public static Intent createIntent(Context context, long boardId) {
		final Intent intent = new Intent(context, ScribbleBoardActivity.class);
		intent.putExtra(INTENT_EXTRA_BOARD_ID, boardId);
		return intent;
	}
}