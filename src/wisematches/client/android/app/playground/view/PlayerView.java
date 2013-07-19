package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import wisematches.client.android.R;
import wisematches.client.android.app.account.view.PersonalityView;
import wisematches.client.android.data.model.scribble.ScribbleBoard;
import wisematches.client.android.data.model.scribble.ScribbleHand;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerView {
	private final View inflate;

	private final TextView pointsView;
	private final TextView remainedTime;

	private final ScribbleHand scribbleHand;

	public PlayerView(Context context, ScribbleBoard board, ScribbleHand scribbleHand) {
		this.scribbleHand = scribbleHand;

		inflate = View.inflate(context, R.layout.playground_board_widget_players_row, null);

		pointsView = (TextView) inflate.findViewById(R.id.scribbleBoardPlayerPoints);
		remainedTime = (TextView) inflate.findViewById(R.id.scribbleBoardPlayerReminder);

		PersonalityView personalityView = (PersonalityView) inflate.findViewById(R.id.scribbleBoardPlayerView);
		personalityView.setPersonality(scribbleHand.getPersonality(), true);

		validate(board);
	}

	public void validate(ScribbleBoard board) {
		pointsView.setText(String.valueOf(scribbleHand.getScores().getPoints()));

		final ScribbleHand hand = board.getPlayerTurn();
		if (scribbleHand == hand) {
			remainedTime.setText(String.valueOf(board.getStatus().getRemainedTime().getText()));
		} else {
			remainedTime.setText("");
		}
	}

	public View getInflate() {
		return inflate;
	}
}
