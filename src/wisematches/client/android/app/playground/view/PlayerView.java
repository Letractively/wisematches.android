package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import wisematches.client.android.R;
import wisematches.client.android.app.account.view.PersonalityView;
import wisematches.client.android.data.model.scribble.ScribbleHand;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerView {
	private final View inflate;

	private final TextView pointsView;

	private final ScribbleHand scribbleHand;

	public PlayerView(Context context, ScribbleHand scribbleHand) {
		this.scribbleHand = scribbleHand;

		inflate = View.inflate(context, R.layout.playground_board_widget_players_row, null);

		pointsView = (TextView) inflate.findViewById(R.id.scribbleBoardPlayerPoints);

		PersonalityView personalityView = (PersonalityView) inflate.findViewById(R.id.scribbleBoardPlayerView);
		personalityView.setPersonality(scribbleHand.getPersonality(), true);

		validate();
	}

	public void validate() {
		pointsView.setText(String.valueOf(scribbleHand.getScores().getPoints()));
	}

	public View getInflate() {
		return inflate;
	}
}
