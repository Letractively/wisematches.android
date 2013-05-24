package wisematches.client.android.app.playground.scribble.board;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesActivity;
import wisematches.client.android.app.account.view.PersonalityView;
import wisematches.client.android.data.model.scribble.ScribbleBoard;
import wisematches.client.android.data.model.scribble.ScribbleHand;
import wisematches.client.android.data.model.scribble.ScribbleTile;
import wisematches.client.android.data.model.scribble.ScribbleWord;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleBoardActivity extends WiseMatchesActivity {
	private static final String INTENT_EXTRA_BOARD_ID = "INTENT_EXTRA_BOARD_ID";

	public ScribbleBoardActivity() {
		super(null, R.layout.playground_board, true);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		openBoard(getIntent().getLongExtra(INTENT_EXTRA_BOARD_ID, 0));
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuItem test = menu.add("Memory Words");
		test.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {

				return true;
			}
		});
		test.setIcon(R.drawable.board_memory_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return true;
	}

	private void showBoardInfo(ScribbleBoard board) {
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(board.getSettings().getTitle() + " #" + board.getId());

		final Button makeTurnBtn = (Button) findViewById(R.id.scribbleBoardBtnMake);
		final Button passTurnBtn = (Button) findViewById(R.id.scribbleBoardBtnPass);
		final Button exchangeBtn = (Button) findViewById(R.id.scribbleBoardBtnExchange);

		final TextView dictField = (TextView) findViewById(R.id.scribbleBoardDictField);

		final BoardView boardView = (BoardView) findViewById(R.id.scribbleBoardView);
		boardView.initBoardView(board, getBitmapFactory());
		boardView.requestFocus();

		final ProgressView progressView = (ProgressView) findViewById(R.id.scribbleBoardProgressView);
		progressView.updateProgress(board);

		final Button dictionaryBth = (Button) findViewById(R.id.scribbleBoardBtnDict);

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
			public void onTileSelected(ScribbleTile tile, boolean selected) {
			}

			@Override
			public void onWordSelected(ScribbleWord word) {
				boolean enabled = word != null;

				makeTurnBtn.setEnabled(enabled);
				passTurnBtn.setEnabled(enabled);
				exchangeBtn.setEnabled(enabled);

				if (!enabled) {
					dictField.setText("");
				} else {
					dictField.setText(word.getRow() + " " + word.getColumn() + " " + word.getDirection() + " " + word.getText());
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