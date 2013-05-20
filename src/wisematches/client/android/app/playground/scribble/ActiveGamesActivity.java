package wisematches.client.android.app.playground.scribble;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesActivity;
import wisematches.client.android.app.MenuFactory;
import wisematches.client.android.app.account.view.PersonalityView;
import wisematches.client.android.app.playground.scribble.board.ScribbleBoardActivity;
import wisematches.client.android.data.DataRequestManager;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.model.scribble.ScribbleDescriptor;
import wisematches.client.android.data.model.scribble.ScribbleScore;
import wisematches.client.android.security.SecurityContext;
import wisematches.client.android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ActiveGamesActivity extends WiseMatchesActivity {
	private static final String INTENT_EXTRA_PID = "PLAYER_ID";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playground_active_games);

		final View progressBar = findViewById(R.id.listProgressBar);
		progressBar.setVisibility(View.VISIBLE);

		final SecurityContext securityContext = getSecurityContext();
		final long pid = getIntent().getLongExtra(INTENT_EXTRA_PID, securityContext.getPersonality().getId());

		getSupportActionBar().setTitle("Текущие игры");

		final ListView listView = (ListView) findViewById(R.id.scribbleViewActive);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg) {
				final ScribbleDescriptor desc = (ScribbleDescriptor) adapterView.getItemAtPosition(position);
				if (desc != null) {
					startActivity(ScribbleBoardActivity.createIntent(ActiveGamesActivity.this, desc.getBoardId()));
				}
			}
		});

		getRequestManager().getActiveGames(pid, new DataRequestManager.DataResponse<ArrayList<ScribbleDescriptor>>() {
			@Override
			public void onSuccess(ArrayList<ScribbleDescriptor> data) {
				listView.setAdapter(new ActiveGamesAdapter(ActiveGamesActivity.this, data));

				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onFailure(String code, String message) {
				getActionBar().setTitle("onFailure: " + message);
			}

			@Override
			public void onDataError() {
				getActionBar().setTitle("onDataError");
			}

			@Override
			public void onConnectionError(int code) {
				getActionBar().setTitle("onConnectionError: " + code);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuFactory.addMenuItem(menu, 1, 1, MenuFactory.Type.CREATE_GAME, MenuItem.SHOW_AS_ACTION_IF_ROOM);
		MenuFactory.addMenuItem(menu, 1, 2, MenuFactory.Type.JOIN_GAME, MenuItem.SHOW_AS_ACTION_IF_ROOM);
		MenuFactory.addMenuItem(menu, 2, 3, MenuFactory.Type.FINISHED_GAMES, MenuItem.SHOW_AS_ACTION_NEVER);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuFactory.startMenuActivity(this, item);
	}

	public static Intent createIntent(Context context) {
		return createIntent(context, 0);
	}

	public static Intent createIntent(Context context, long pid) {
		final Intent intent = new Intent(context, ActiveGamesActivity.class);
		if (pid != 0) {
			intent.putExtra(INTENT_EXTRA_PID, pid);
		}
		return intent;
	}

	/**
	 * @author Sergey Klimenko (smklimenko@gmail.com)
	 */
	private static class ActiveGamesAdapter extends ArrayAdapter<ScribbleDescriptor> {
		public ActiveGamesAdapter(Context context, List<ScribbleDescriptor> objects) {
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

			holder.title.setText(value.getTitle());
			holder.number.setText(String.valueOf(value.getBoardId()));

			holder.players.removeAllViews();

			final int count = value.getPlayersCount();
			final Personality[] players = value.getPlayers();
			final ScribbleScore[] scores = value.getScores();

			for (int index = 0; index < count; index++) {
				final Personality personality = players[index];
				final ScribbleScore score = scores[index];
				final TableRow row = new TableRow(context);

				if (value.getPlayerTurnIndex() == index) {
					row.setBackgroundResource(R.drawable.player_state_active);
				}

				final PersonalityView player = new PersonalityView(context, null);
				player.setPersonality(personality);
				row.addView(player);

				final TextView p = new TextView(context);
				p.setText(String.valueOf(score.getPoints()));
				p.setPadding(5, 0, 0, 0);
				row.addView(p);

				holder.players.addView(row);
			}
		}

		private static class ViewHolder {
			private TextView title;
			private TextView number;
			private TextView elapsedTime;
			private TableLayout players;
		}
	}
}