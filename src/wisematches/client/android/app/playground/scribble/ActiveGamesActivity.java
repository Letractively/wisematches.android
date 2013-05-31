package wisematches.client.android.app.playground.scribble;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesActivity;
import wisematches.client.android.app.MenuFactory;
import wisematches.client.android.app.OnResultListener;
import wisematches.client.android.app.playground.scribble.board.ScribbleBoardActivity;
import wisematches.client.android.data.model.Id;
import wisematches.client.android.data.model.scribble.ActiveGames;
import wisematches.client.android.data.model.scribble.ScribbleDescriptor;
import wisematches.client.android.data.model.scribble.ScribbleProposal;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ActiveGamesActivity extends WiseMatchesActivity {
	private ListView gamesListView;

	private static final String INTENT_EXTRA_PID = "PARAM_PLAYER_ID";

	public ActiveGamesActivity() {
		super("Текущие игры", R.layout.playground_active_games, true);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		gamesListView = (ListView) findViewById(R.id.playgroundGamesList);
		gamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg) {
				final ScribbleDescriptor desc = (ScribbleDescriptor) adapterView.getItemAtPosition(position);
				if (desc != null) {
					startActivity(ScribbleBoardActivity.createIntent(ActiveGamesActivity.this, desc.getId()));
				}
			}
		});

		loadActiveGames();
	}

	private void loadActiveGames() {
		getRequestManager().getActiveGames(getPersonality(true).getId(), new SmartDataResponse<ActiveGames>(this) {
			@Override
			protected void onData(ActiveGames data) {
				gamesListView.setAdapter(new GamesListAdapter(ActiveGamesActivity.this, data.getDescriptors(), data.getProposals()));
			}

			@Override
			protected void onRetry() {
				loadActiveGames();
			}

			@Override
			protected void onCancel() {
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuFactory.addMenuItem(menu, 1, 1, MenuFactory.Type.CREATE_GAME, MenuItem.SHOW_AS_ACTION_ALWAYS);
		MenuFactory.addMenuItem(menu, 1, 2, MenuFactory.Type.JOIN_GAME, MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
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


	private void cancelWaitingGame(final long proposalId, final OnResultListener<Boolean> listener) {
		getRequestManager().processWaitingGame(proposalId, false, new SmartDataResponse<Id>(this) {
			@Override
			protected void onData(Id data) {
				loadActiveGames();
				listener.onResult(true);
			}

			@Override
			protected void onRetry() {
				cancelWaitingGame(proposalId, listener);
			}

			@Override
			protected void onCancel() {
				listener.onResult(false);
			}
		});
	}

	private class GamesListAdapter extends BaseAdapter {
		private final ActiveGamesAdapter activeGamesAdapter;
		private final WaitingGamesAdapter waitingGamesAdapter;

		private GamesListAdapter(Context context, ScribbleDescriptor[] descriptors, ScribbleProposal[] proposals) {
			activeGamesAdapter = new ActiveGamesAdapter(context, descriptors);
			waitingGamesAdapter = new WaitingGamesAdapter(context, proposals, false, "Отказаться", new WaitingGamesAdapter.ProposalClickListener() {
				@Override
				public void onAccept(long id, OnResultListener<Boolean> listener) {
					cancelWaitingGame(id, listener);
				}

				@Override
				public void onReject(long id, OnResultListener<Boolean> listener) {
				}
			});
		}

		@Override
		public int getCount() {
			return activeGamesAdapter.getCount() + waitingGamesAdapter.getCount();
		}

		@Override
		public Object getItem(int position) {
			int count = activeGamesAdapter.getCount();
			if (position < count) {
				return activeGamesAdapter.getItem(position);
			} else {
				return waitingGamesAdapter.getItem(position - count);
			}
		}

		@Override
		public long getItemId(int position) {
			int count = activeGamesAdapter.getCount();
			if (position < count) {
				return activeGamesAdapter.getItemId(position);
			} else {
				return waitingGamesAdapter.getItemId(position - count);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int count = activeGamesAdapter.getCount();
			if (position < count) {
				return activeGamesAdapter.getView(position, null, parent);
			} else {
				return waitingGamesAdapter.getView(position - count, null, parent);
			}
		}
	}
}