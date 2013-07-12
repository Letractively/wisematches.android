package wisematches.client.android.app.playground.activity;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class sadf {

	/*	private Button makeTurnBtn;
	private Button passTurnBtn;
	private Button exchangeTilesBtn;

	private Button selectTilesBtn;
	private Button clearSelectionBtn;

	private BoardViewOld boardView;
	private ProgressView progressView;

	private Button dictionaryAction;
	private EditText dictionaryField;
	private TimerTask dictionaryChecker;
	private ProgressBar dictionaryProgress;

	private final Timer scribbleBoardTimer = new Timer("ScribbleBoardTimer");


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

		boardView = (BoardViewOld) findViewById(R.id.scribbleBoardView);
		progressView = (ProgressView) findViewById(R.id.scribbleBoardProgressView);

		dictionaryField = (EditText) findViewById(R.id.scribbleBoardDictField);
		dictionaryAction = (Button) findViewById(R.id.scribbleBoardDictAction);
		dictionaryProgress = (ProgressBar) findViewById(R.id.scribbleBoardDictProgress);
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

//		boardView.initBoardView(board);
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
		boardView.setScribbleBoardListener(new SelectionListener() {
			@Override
			public void onSelectionChanged(ScribbleTile tile, boolean selected, Set<ScribbleTile> selectedTiles) {
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

				getRequestManager().getWordEntry(text.toString(), lang, new SmartDataResponse<WordEntry>(GameControllerActivity.this) {
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

		boardView.setScribbleBoardListener(new SelectionListener() {
			@Override
			public void onSelectionChanged(ScribbleTile tile, boolean selected, Set<ScribbleTile> selectedTiles) {
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

*/
}
