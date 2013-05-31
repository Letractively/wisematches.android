package wisematches.client.android.app.playground.scribble.board;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesActivity;
import wisematches.client.android.app.account.view.PersonalityView;
import wisematches.client.android.data.model.scribble.*;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleBoardActivity extends WiseMatchesActivity {
	private Button makeTurnBtn;
	private Button passTurnBtn;
	private Button exchangeTilesBtn;

	private Button selectTilesBtn;
	private Button clearSelectionBtn;

	private BoardView boardView;
	private ProgressView progressView;

	private Button dictionaryAction;
	private EditText dictionaryField;
	private TimerTask dictionaryChecker;
	private ProgressBar dictionaryProgress;

	private TableLayout movesHistoryView;

	private TextView pointsCalculationFld;

	private final Timer scribbleBoardTimer = new Timer("ScribbleBoardTimer");

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

		boardView = (BoardView) findViewById(R.id.scribbleBoardView);
		progressView = (ProgressView) findViewById(R.id.scribbleBoardProgressView);

		dictionaryField = (EditText) findViewById(R.id.scribbleBoardDictField);
		dictionaryAction = (Button) findViewById(R.id.scribbleBoardDictAction);
		dictionaryProgress = (ProgressBar) findViewById(R.id.scribbleBoardDictProgress);

		pointsCalculationFld = (TextView) findViewById(R.id.scribbleBoardFltPoints);


		movesHistoryView = (TableLayout) findViewById(R.id.scribbleBoardMovesHistory);

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

		initPlayers(board);
		initControls(board);
		initDictionary(board);
		initHistoryMoves(board);
	}

	private void initPlayers(ScribbleBoard board) {
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
	}

	private void initControls(final ScribbleBoard board) {
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
			}
		});
	}

	private void initDictionary(final ScribbleBoard board) {
		final Runnable wordChecker = new Runnable() {
			@Override
			public void run() {
				dictionaryAction.setEnabled(false);
				dictionaryProgress.setVisibility(View.VISIBLE);
				dictionaryProgress.getIndeterminateDrawable().setColorFilter(null);

				final String lang = board.getSettings().getLanguage().getCode();
				final Editable text = dictionaryField.getText();

				getRequestManager().getWordEntry(text.toString(), lang, new SmartDataResponse<WordEntry>(ScribbleBoardActivity.this) {
					@Override
					protected void onData(WordEntry data) {
						if (data != null) {
							dictionaryProgress.getIndeterminateDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
						} else {
							dictionaryProgress.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
						}

						dictionaryChecker = null;
						dictionaryAction.setEnabled(true);
					}

					@Override
					protected void onRetry() {
						getRequestManager().getWordEntry(text.toString(), lang, this);
					}

					@Override
					protected void onCancel() {
						dictionaryChecker = null;
						dictionaryAction.setEnabled(true);

						dictionaryProgress.setVisibility(View.INVISIBLE);
					}
				});
			}
		};

		boardView.setScribbleBoardListener(new BoardViewListener() {
			@Override
			public void onTileSelected(ScribbleTile tile, boolean selected, Set<ScribbleTile> selectedTiles) {
			}

			@Override
			public void onWordSelected(ScribbleWord word) {
				boolean empty = word == null || word.length() == 0;

				if (empty) {
					dictionaryField.setText("");
					pointsCalculationFld.setText("");
				} else {
					dictionaryField.setText(word.getText());

					final ScoreCalculation calculation = board.getScoreEngine().calculateWordScore(board, word);
					pointsCalculationFld.setText(calculation.getPoints() + " = " + calculation.getFormula());
				}
			}
		});

		dictionaryField.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(final Editable s) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				dictionaryProgress.setVisibility(View.INVISIBLE);

				final boolean empty = s.length() <= 1;

				dictionaryAction.setEnabled(!empty);

				if (dictionaryChecker != null) {
					dictionaryChecker.cancel();
					dictionaryChecker = null;
				}

				if (!empty) {
					dictionaryChecker = new TimerTask() {
						@Override
						public void run() {
							runOnUiThread(wordChecker);
						}
					};
					scribbleBoardTimer.schedule(dictionaryChecker, 3000);
				}
			}
		});

		dictionaryAction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dictionaryChecker != null) {
					dictionaryChecker.cancel();
					dictionaryChecker = null;
				}
				wordChecker.run();
			}
		});
	}

	private void initHistoryMoves(ScribbleBoard board) {
		movesHistoryView.removeAllViews();

		final TableRow header = (TableRow) getLayoutInflater().inflate(R.layout.playground_board_move, null);
		int childCount = header.getChildCount();
		for (int i = 0; i < childCount; i++) {
			((TextView) header.getChildAt(i)).setTextAppearance(this, R.style.TextAppearance_WiseMatches_Move_Header);
		}
		movesHistoryView.addView(header);

		List<ScribbleMove> moves = board.getMoves();
		final ListIterator<ScribbleMove> movesIterator = moves.listIterator(moves.size());
		while (movesIterator.hasPrevious()) {
			final ScribbleMove move = movesIterator.previous();
			final View row = getLayoutInflater().inflate(R.layout.playground_board_move, null);

			final TextView moveInfoView = (TextView) row.findViewById(R.id.scribbleBoardMoveInfo);
			final TextView moveNumberView = (TextView) row.findViewById(R.id.scribbleBoardMoveNumber);
			final TextView movePlayerView = (TextView) row.findViewById(R.id.scribbleBoardMovePlayer);
			final TextView movePointsView = (TextView) row.findViewById(R.id.scribbleBoardMovePoints);

			MoveType moveType = move.getMoveType();
			switch (moveType) {
				case MAKE:
					moveInfoView.setText(((ScribbleMove.Make) move).getWord().getText());
					break;
				case PASS:
					moveInfoView.setText("пропуск");
					moveInfoView.setTextAppearance(this, R.style.TextAppearance_WiseMatches_Move_Empty);
					break;
				case EXCHANGE:
					moveInfoView.setText("обмен");
					moveInfoView.setTextAppearance(this, R.style.TextAppearance_WiseMatches_Move_Empty);
					break;
			}

			moveNumberView.setText(String.valueOf(move.getNumber() + 1));
			movePointsView.setText(String.valueOf(move.getPoints()));
			movePlayerView.setText(board.getPlayer(move.getPlayer()).getPlayer().getNickname());

			movesHistoryView.addView(row);
		}

		movesHistoryView.invalidate();
	}

	public static Intent createIntent(Context context, long boardId) {
		final Intent intent = new Intent(context, ScribbleBoardActivity.class);
		intent.putExtra(INTENT_EXTRA_BOARD_ID, boardId);
		return intent;
	}
}