package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import wisematches.client.android.R;
import wisematches.client.android.data.model.scribble.MoveType;
import wisematches.client.android.data.model.scribble.ScribbleBoard;
import wisematches.client.android.data.model.scribble.ScribbleMove;

import java.util.List;
import java.util.ListIterator;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MovesWidget extends AbstractScribbleWidget {
	private TableLayout movesHistoryView;

	private ScribbleBoard scribbleBoard;

	private final TheOnClickListener clickListener = new TheOnClickListener();

	public MovesWidget(Context context, AttributeSet attrs) {
		this(context, attrs, true);
	}

	public MovesWidget(Context context, AttributeSet attrs, boolean showTitle) {
		super(context, attrs, R.layout.playground_board_widget_moves, "История ходов", showTitle);

		movesHistoryView = (TableLayout) findViewById(R.id.scribbleBoardMovesHistory);
	}

	@Override
	public void boardInitialized(ScribbleBoard board) {
		this.scribbleBoard = board;

		final Context context = getContext();
		final TableRow header = (TableRow) inflate(context, R.layout.playground_board_widget_moves_row, null);
		int childCount = header.getChildCount();
		for (int i = 0; i < childCount; i++) {
			((TextView) header.getChildAt(i)).setTextAppearance(context, R.style.TextAppearance_WiseMatches_Move_Header);
		}
		movesHistoryView.addView(header);

		List<ScribbleMove> moves = board.getMoves();
		final ListIterator<ScribbleMove> movesIterator = moves.listIterator(moves.size());
		while (movesIterator.hasPrevious()) {
			registerMove(movesIterator.previous());
		}

		movesHistoryView.invalidate();
	}

	@Override
	public void boardTerminated(ScribbleBoard board) {
		movesHistoryView.removeAllViews();

		this.scribbleBoard = null;
	}

	private void registerMove(ScribbleMove move) {
		final Context context = getContext();

		final View row = inflate(context, R.layout.playground_board_widget_moves_row, null);
		row.setTag(move);
		row.setOnClickListener(clickListener);

		final TextView moveInfoView = (TextView) row.findViewById(R.id.scribbleBoardMoveInfo);
		final TextView moveNumberView = (TextView) row.findViewById(R.id.scribbleBoardMoveNumber);
		final TextView movePlayerView = (TextView) row.findViewById(R.id.scribbleBoardMovePlayer);
		final TextView movePointsView = (TextView) row.findViewById(R.id.scribbleBoardMovePoints);

		MoveType moveType = move.getMoveType();
		switch (moveType) {
			case MAKE:
				moveInfoView.setText(Html.fromHtml("<u>" + ((ScribbleMove.Make) move).getWord().getText() + "</u>"));
				moveInfoView.setTextAppearance(context, R.style.TextAppearance_WiseMatches_Move_Link);
				break;
			case PASS:
				moveInfoView.setText("пропуск");
				moveInfoView.setTextAppearance(context, R.style.TextAppearance_WiseMatches_Move_Empty);
				break;
			case EXCHANGE:
				moveInfoView.setText("обмен");
				moveInfoView.setTextAppearance(context, R.style.TextAppearance_WiseMatches_Move_Empty);
				break;
		}

		moveNumberView.setText(String.valueOf(move.getNumber() + 1));
		movePointsView.setText(String.valueOf(move.getPoints()));
		movePlayerView.setText(scribbleBoard.getPlayer(move.getPlayer()).getPersonality().getNickname());

		movesHistoryView.addView(row);
	}

	private class TheOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			final ScribbleMove move = (ScribbleMove) v.getTag();
			if (move instanceof ScribbleMove.Make) {
				scribbleBoard.getSelectionModel().setSelection(((ScribbleMove.Make) move).getWord());
			}
			notifyWidgetActionDone();
		}
	}
}
