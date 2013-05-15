package wisematches.client.android.app.playground.scribble;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesActivity;
import wisematches.client.android.app.playground.ScribbleGameInfo;
import wisematches.client.android.app.playground.scribble.board.ScribbleBoardActivity;
import wisematches.client.android.os.ProgressTask;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ActiveGamesActivity extends WiseMatchesActivity {
	private static final String INTENT_EXTRA_PID = "PLAYER_ID";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playground_dashboard);

		// TODO: for testing only
		if (true) {
//			openBoard(2459);
			return;
		}

		final ListView listView = (ListView) findViewById(R.id.scribbleViewActive);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg) {
				final ScribbleGameInfo info = (ScribbleGameInfo) adapterView.getItemAtPosition(position);
				openBoard(info.getBoardId());
			}
		});

		ProgressTask<Void, Void, ScribbleGameInfo[]> a = new ProgressTask<Void, Void, ScribbleGameInfo[]>("Loading active games. Please wait...", this) {
			@Override
			protected ScribbleGameInfo[] doInBackground(Void... voids) {
/*
				try {
					final WiseMatchesServer.Response r = getWiseMatchesClient().execute("/playground/scribble/active.ajax");
					if (r.isSuccess()) {
						final JSONObject data = r.getData();


						final JSONArray games = data.getJSONArray("games");
						ScribbleGameInfo[] infos = new ScribbleGameInfo[games.length()];
						for (int i = 0; i < games.length(); i++) {
							final JSONObject obj = games.getJSONObject(i);
							infos[i] = new ScribbleGameInfo(obj);
						}
						return infos;
					}
				} catch (JSONException | CooperationException | CommunicationException ex) {
					ex.printStackTrace();
				}
*/
				return null;
			}

			@Override
			protected void onPostExecute(ScribbleGameInfo[] games) {
				super.onPostExecute(games);

				ActiveGamesAdapter adapter = new ActiveGamesAdapter(ActiveGamesActivity.this, games);
				listView.setAdapter(adapter);
			}
		};
		a.execute();
	}

	private void openBoard(final long boardId) {
		final Intent intent = new Intent(this, ScribbleBoardActivity.class);
		intent.putExtra("boardId", boardId);
		startActivity(intent);
	}

	public static Intent createIntent(Context context) {
		return createIntent(context, 0);
	}

	public static Intent createIntent(Context context, long pid) {
		final Intent intent = new Intent(context, ActiveGamesActivity.class);
		intent.putExtra(INTENT_EXTRA_PID, pid);
		return intent;
	}
}