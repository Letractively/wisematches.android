package wisematches.client.android.app.playground.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import wisematches.client.android.R;
import wisematches.client.android.app.SignalProcessor;
import wisematches.client.android.app.account.view.PersonalityView;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.model.scribble.CriterionViolation;
import wisematches.client.android.data.model.scribble.ScribbleProposal;
import wisematches.client.android.widget.ArrayAdapter;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WaitingGamesAdapter extends ArrayAdapter<ScribbleProposal> {
	private final Context context;
	private final String action;
	private final boolean globalLock;
	private final ProposalClickListener callback;

	private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

	public WaitingGamesAdapter(Context context, ScribbleProposal[] objects, boolean globalLock, String action, ProposalClickListener actionListener) {
		super(context, R.layout.playground_waiting_game, R.layout.playground_waiting_game, false, objects);
		this.context = context;
		this.action = action;
		this.callback = actionListener;
		this.globalLock = globalLock;
	}

	@Override
	protected void populateValueToView(final View view, final ScribbleProposal value) {
		ViewHolder holder = (ViewHolder) view.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			holder.title = (TextView) view.findViewById(R.id.boardTitle);
			holder.language = (TextView) view.findViewById(R.id.playgroundJoinLanguage);
			holder.timeout = (TextView) view.findViewById(R.id.playgroundJoinTimeout);
			holder.error = (TextView) view.findViewById(R.id.playgroundJoinErr);
			holder.action = (Button) view.findViewById(R.id.playgroundJoinAction);
			holder.players = (LinearLayout) view.findViewById(R.id.dashboardPlayers);

			holder.action.setText(this.action);
			holder.action.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View v) {
					v.setEnabled(false);
					callback.onAccept((long) v.getTag(), new SignalProcessor<Boolean>() {
						@Override
						public void onResult(Boolean result) {
							if (!result) {
								v.setEnabled(true);
							}
						}
					});
				}
			});
			view.setTag(holder);
		}

		holder.title.setText(value.getSettings().getTitle());
		holder.language.setText(context.getResources().getString(value.getSettings().getLanguage().getResourceId()));
		holder.timeout.setText(value.getSettings().getDaysPerMove() + " дня");

		if (!globalLock) {
			final CriterionViolation[] violations = value.getViolations();
			if (violations.length == 0) {
				holder.action.setVisibility(View.VISIBLE);
				holder.action.setTag(value.getId());
				holder.error.setText("");
			} else {
				holder.action.setVisibility(View.GONE);
				holder.error.setText(violations[0].getShortDescription());
			}
		} else {
			holder.action.setVisibility(View.GONE);
			holder.error.setText("");
		}

		final Personality[] players = value.getPlayers();
		for (int i = 0; i < players.length; i++) {
			final Personality player = players[i];

			final View childAt = holder.players.getChildAt(i);
			if (player != null) {
				if (childAt instanceof PersonalityView) {
					final PersonalityView at = (PersonalityView) childAt;
					at.setPersonality(player);
				} else {
					holder.players.removeViewAt(i);
					holder.players.addView(new PersonalityView(context, null, player), i);
				}
			} else {
				if (!(childAt instanceof TextView)) {
					holder.players.removeViewAt(i);
					TextView child = new TextView(context);
					child.setSingleLine();
					child.setText("ожидание противника");
					child.setTextAppearance(context, R.style.TextAppearance_WM_Player_Waiting);
					holder.players.addView(child, i, params);
				}
			}
			childAt.setVisibility(View.VISIBLE);
		}
		for (int i = players.length; i < 4; i++) {
			holder.players.getChildAt(i).setVisibility(View.GONE);
		}
		holder.players.requestLayout();
	}

	private class ViewHolder {
		private TextView title;
		private TextView language;
		private TextView timeout;
		private TextView error;
		private Button action;
		private LinearLayout players;
	}

	public interface ProposalClickListener {
		void onAccept(long id, SignalProcessor<Boolean> listener);

		void onReject(long id, SignalProcessor<Boolean> listener);
	}
}
