package wisematches.client.android.app.playground.widget;

import android.content.Context;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import wisematches.client.android.R;
import wisematches.client.android.app.account.view.PersonalityView;
import wisematches.client.android.data.model.scribble.ScribbleDescriptor;
import wisematches.client.android.data.model.scribble.ScribbleHand;
import wisematches.client.android.widget.ArrayAdapter;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ActiveGamesAdapter extends ArrayAdapter<ScribbleDescriptor> {
	public ActiveGamesAdapter(Context context, ScribbleDescriptor[] objects) {
		super(context, R.layout.playground_active_game, R.layout.playground_active_game, false, objects);
	}

	@Override
	protected void populateValueToView(View view, ScribbleDescriptor value) {
		ViewHolder holder = (ViewHolder) view.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			holder.title = (TextView) view.findViewById(R.id.boardTitle);
			holder.number = (TextView) view.findViewById(R.id.boardNumber);
			holder.elapsedTime = (TextView) view.findViewById(R.id.boardElapsedTime);
			holder.players = (TableLayout) view.findViewById(R.id.dashboardPlayers);

			view.setTag(holder);
		}

		holder.title.setText(value.getSettings().getTitle());
		holder.number.setText(String.valueOf(value.getId()));
		holder.elapsedTime.setText(String.valueOf(value.getRemainedTime().getText()));

		final ScribbleHand[] players = value.getPlayers();
		for (int index = 0; index < players.length; index++) {
			final ScribbleHand hand = players[index];
			final TableRow row = (TableRow) holder.players.getChildAt(index);
			row.setVisibility(View.VISIBLE);

			if (value.getPlayerTurnIndex() == index) {
				row.setBackgroundResource(R.drawable.player_state_active);
			}


			final PersonalityView player = (PersonalityView) row.findViewById(R.id.dashboardPlayerView);
			player.setPersonality(hand.getPlayer());

			final TextView points = (TextView) row.findViewById(R.id.dashboardPlayerPoints);
			points.setText(String.valueOf(hand.getScores().getPoints()));
		}

		for (int i = players.length; i < holder.players.getChildCount(); i++) {
			holder.players.getChildAt(i).setVisibility(View.GONE);
		}
	}

	private static class ViewHolder {
		private TextView title;
		private TextView number;
		private TextView elapsedTime;
		private TableLayout players;
	}
}