package wisematches.client.android.app.playground;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import org.json.JSONException;
import org.json.JSONObject;
import wisematches.client.android.R;
import wisematches.client.android.app.WiseMatchesActivity;
import wisematches.client.android.app.account.view.PlayerView;
import wisematches.client.android.app.playground.scribble.model.*;
import wisematches.client.android.app.playground.scribble.view.BoardView;
import wisematches.client.android.app.playground.scribble.view.BoardViewListener;
import wisematches.client.android.app.playground.scribble.view.ProgressView;
import wisematches.client.android.http.ClientResponse;
import wisematches.client.android.os.ProgressTask;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleBoardActivity extends WiseMatchesActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playground_board);

		final Bundle extras = getIntent().getExtras();
		final long boardId = 2552;// extras.getLong("boardId");

		ProgressTask<Long, Void, ScribbleGame> a = new ProgressTask<Long, Void, ScribbleGame>("Loading board #" + boardId, this) {
			@Override
			protected ScribbleGame doInBackground(Long... voids) {
				try {
					JSONObject board = new JSONObject("{" +
							"\"handTiles\":null," +
							"\"id\":2552," +
							"\"active\":false," +
							"\"playerTurn\":1001," +
							"\"resolution\":\"INTERRUPTED\"," +
							"\"relationship\":{" + "\"code\":1,\"id\":5}," +
							"\"remainedTime\":{\"millis\":-81600000,\"text\":\"0 days\"}," +
							"\"startedTime\":{\"millis\":1364760000000,\"text\":\"April 1, 2013\"}," +
							"\"finishedTime\":{\"millis\":1366268736000,\"text\":\"April 18, 2013\"}," +
							"\"lastChange\":1366268736000," +
							"\"settings\":{" +
							"	\"language\":\"ru\"," +
							"	\"daysPerMove\":3," +
							"	\"title\":\"5th WiseMatches Tourney\"," +
							"	\"scratch\":false}," +
							"\"players\":[" +
							"	{	" +
							"		\"score\":{" +
							"			\"points\":-24," +
							"			\"newRating\":1212," +
							"			\"oldRating\":1223," +
							"			\"winner\":false}," +
							"		\"info\":{" +
							"			\"online\":false," +
							"			\"nickname\":\"smklimenko\"," +
							"			\"id\":1001," +
							"			\"type\":\"MEMBER\"," +
							"			\"membership\":\"BASIC\"," +
							"			\"robotType\":null}," +
							"		\"id\":1001}," +
							"	{\"score\":{\"points\":317,\"newRating\":1345,\"oldRating\":1334,\"winner\":true},\"info\":{\"online\":false,\"nickname\":\"iry\",\"id\":1003,\"type\":\"MEMBER\",\"membership\":\"BASIC\",\"robotType\":null},\"id\":1003}]," +
							"\"bonuses\":[" +
							"	{\"type\":\"W3\",\"row\":0,\"column\":3},{\"type\":\"L3\",\"row\":0,\"column\":6}," +
							"	{\"type\":\"L2\",\"row\":1,\"column\":2},{\"type\":\"W2\",\"row\":1,\"column\":5}," +
							"	{\"type\":\"L2\",\"row\":2,\"column\":1},{\"type\":\"L2\",\"row\":2,\"column\":4}," +
							"	{\"type\":\"W3\",\"row\":3,\"column\":0},{\"type\":\"L3\",\"row\":3,\"column\":3}," +
							"	{\"type\":\"W2\",\"row\":3,\"column\":7},{\"type\":\"L2\",\"row\":4,\"column\":2}," +
							"	{\"type\":\"L2\",\"row\":4,\"column\":6},{\"type\":\"W2\",\"row\":5,\"column\":1}," +
							"	{\"type\":\"L3\",\"row\":5,\"column\":5},{\"type\":\"L3\",\"row\":6,\"column\":0}," +
							"	{\"type\":\"L2\",\"row\":6,\"column\":4},{\"type\":\"W2\",\"row\":7,\"column\":3}]," +
							"\"spentTime\":{\"millis\":1508700000,\"text\":\"17d 11h\"}," +
							"\"bank\":{" +
							"	\"lettersCount\":134," +
							"	\"language\":\"RU\"," +
							"	\"letterDescriptions\":[" +
							"		{\"letter\":\"а\",\"cost\":1,\"count\":10},{\"letter\":\"б\",\"cost\":3,\"count\":3}," +
							"		{\"letter\":\"в\",\"cost\":2,\"count\":5},{\"letter\":\"г\",\"cost\":3,\"count\":3}," +
							"		{\"letter\":\"д\",\"cost\":2,\"count\":5},{\"letter\":\"е\",\"cost\":1,\"count\":9}," +
							"		{\"letter\":\"ж\",\"cost\":5,\"count\":2},{\"letter\":\"з\",\"cost\":5,\"count\":2}," +
							"		{\"letter\":\"и\",\"cost\":1,\"count\":10},{\"letter\":\"й\",\"cost\":4,\"count\":4}," +
							"		{\"letter\":\"к\",\"cost\":2,\"count\":6},{\"letter\":\"л\",\"cost\":2,\"count\":4}," +
							"		{\"letter\":\"м\",\"cost\":2,\"count\":5},{\"letter\":\"н\",\"cost\":1,\"count\":8}," +
							"		{\"letter\":\"о\",\"cost\":1,\"count\":10},{\"letter\":\"п\",\"cost\":2,\"count\":6}," +
							"		{\"letter\":\"р\",\"cost\":2,\"count\":6},{\"letter\":\"с\",\"cost\":2,\"count\":6}," +
							"		{\"letter\":\"т\",\"cost\":2,\"count\":5},{\"letter\":\"у\",\"cost\":3,\"count\":3}," +
							"		{\"letter\":\"ф\",\"cost\":10,\"count\":1},{\"letter\":\"х\",\"cost\":5,\"count\":2}," +
							"		{\"letter\":\"ц\",\"cost\":5,\"count\":1},{\"letter\":\"ч\",\"cost\":5,\"count\":2}," +
							"		{\"letter\":\"ш\",\"cost\":10,\"count\":2},{\"letter\":\"щ\",\"cost\":10,\"count\":1}," +
							"		{\"letter\":\"ъ\",\"cost\":10,\"count\":1},{\"letter\":\"ы\",\"cost\":5,\"count\":2}," +
							"		{\"letter\":\"ь\",\"cost\":5,\"count\":2},{\"letter\":\"э\",\"cost\":8,\"count\":1}," +
							"		{\"letter\":\"ю\",\"cost\":10,\"count\":1},{\"letter\":\"я\",\"cost\":4,\"count\":4}," +
							"		{\"letter\":\"*\",\"cost\":0,\"count\":2}]}," +
							"\"moves\":[" +
							"	{\"type\":\"MAKE\"," +
							"		\"number\":0," +
							"		\"time\":{\"millis\":1364815513174,\"text\":\"April 1, 2013\"}," +
							"		\"player\":1001," +
							"		\"word\":{" +
							"			\"tiles\":[" +
							"				{\"number\":130,\"letter\":\"я\",\"cost\":4,\"wildcard\":false}," +
							"				{\"number\":96,\"letter\":\"р\",\"cost\":2,\"wildcard\":false}," +
							"				{\"number\":65,\"letter\":\"м\",\"cost\":2,\"wildcard\":false}," +
							"				{\"number\":82,\"letter\":\"о\",\"cost\":1,\"wildcard\":false}]," +
							"			\"position\":{\"row\":7,\"column\":6}," +
							"			\"direction\":\"HORIZONTAL\"," +
							"			\"text\":\"ярмо\"}," +
							"		\"points\":9," +
							"		\"exchange\":0}," +
							"	{\"type\":\"MAKE\",\"number\":1,\"time\":{\"millis\":1364816855723,\"text\":\"April 1, 2013\"},\"player\":1003,\"word\":{\"tiles\":[{\"number\":2,\"letter\":\"а\",\"cost\":1,\"wildcard\":false},{\"number\":96,\"letter\":\"р\",\"cost\":2,\"wildcard\":false},{\"number\":112,\"letter\":\"ф\",\"cost\":10,\"wildcard\":false},{\"number\":3,\"letter\":\"а\",\"cost\":1,\"wildcard\":false}],\"position\":{\"row\":6,\"column\":7},\"direction\":\"VERTICAL\",\"text\":\"арфа\"},\"points\":14,\"exchange\":0},{\"type\":\"MAKE\",\"number\":2,\"time\":{\"millis\":1364836700237,\"text\":\"April 1, 2013\"},\"player\":1001,\"word\":{\"tiles\":[{\"number\":112,\"letter\":\"ф\",\"cost\":10,\"wildcard\":false},{\"number\":3,\"letter\":\"а\",\"cost\":1,\"wildcard\":false},{\"number\":56,\"letter\":\"к\",\"cost\":2,\"wildcard\":false},{\"number\":31,\"letter\":\"е\",\"cost\":1,\"wildcard\":false},{\"number\":60,\"letter\":\"л\",\"cost\":2,\"wildcard\":false}],\"position\":{\"row\":8,\"column\":7},\"direction\":\"VERTICAL\",\"text\":\"факел\"},\"points\":32,\"exchange\":0},{\"type\":\"MAKE\",\"number\":3,\"time\":{\"millis\":1364879644947,\"text\":\"April 2, 2013\"},\"player\":1003,\"word\":{\"tiles\":[{\"number\":131,\"letter\":\"я\",\"cost\":4,\"wildcard\":false},{\"number\":67,\"letter\":\"м\",\"cost\":2,\"wildcard\":false},{\"number\":3,\"letter\":\"а\",\"cost\":1,\"wildcard\":false}],\"position\":{\"row\":9,\"column\":5},\"direction\":\"HORIZONTAL\",\"text\":\"яма\"},\"points\":15,\"exchange\":0},{\"type\":\"MAKE\",\"number\":4,\"time\":{\"millis\":1364904382293,\"text\":\"April 2, 2013\"},\"player\":1001,\"word\":{\"tiles\":[{\"number\":89,\"letter\":\"п\",\"cost\":2,\"wildcard\":false},{\"number\":31,\"letter\":\"е\",\"cost\":1,\"wildcard\":false},{\"number\":75,\"letter\":\"н\",\"cost\":1,\"wildcard\":false},{\"number\":128,\"letter\":\"я\",\"cost\":4,\"wildcard\":false}],\"position\":{\"row\":11,\"column\":6},\"direction\":\"HORIZONTAL\",\"text\":\"пеня\"},\"points\":8,\"exchange\":0},{\"type\":\"MAKE\",\"number\":5,\"time\":{\"millis\":1364913710234,\"text\":\"April 2, 2013\"},\"player\":1003,\"word\":{\"tiles\":[{\"number\":82,\"letter\":\"о\",\"cost\":1,\"wildcard\":false},{\"number\":17,\"letter\":\"в\",\"cost\":2,\"wildcard\":false},{\"number\":78,\"letter\":\"о\",\"cost\":1,\"wildcard\":false},{\"number\":120,\"letter\":\"щ\",\"cost\":10,\"wildcard\":false}],\"position\":{\"row\":7,\"column\":9},\"direction\":\"HORIZONTAL\",\"text\":\"овощ\"},\"points\":28,\"exchange\":0},{\"type\":\"MAKE\",\"number\":6,\"time\":{\"millis\":1364958552071,\"text\":\"April 3, 2013\"},\"player\":1001,\"word\":{\"tiles\":[{\"number\":109,\"letter\":\"у\",\"cost\":3,\"wildcard\":false},{\"number\":18,\"letter\":\"г\",\"cost\":3,\"wildcard\":false},{\"number\":2,\"letter\":\"а\",\"cost\":1,\"wildcard\":false},{\"number\":96,\"letter\":\"р\",\"cost\":2,\"wildcard\":false}],\"position\":{\"row\":4,\"column\":7},\"direction\":\"VERTICAL\",\"text\":\"угар\"},\"points\":9,\"exchange\":0},{\"type\":\"MAKE\",\"number\":7,\"time\":{\"millis\":1364966646705,\"text\":\"April 3, 2013\"},\"player\":1003,\"word\":{\"tiles\":[{\"number\":112,\"letter\":\"ф\",\"cost\":10,\"wildcard\":false},{\"number\":76,\"letter\":\"о\",\"cost\":1,\"wildcard\":false},{\"number\":92,\"letter\":\"р\",\"cost\":2,\"wildcard\":false},{\"number\":103,\"letter\":\"с\",\"cost\":2,\"wildcard\":false}],\"position\":{\"row\":8,\"column\":7},\"direction\":\"HORIZONTAL\",\"text\":\"форс\"},\"points\":17,\"exchange\":0},{\"type\":\"MAKE\",\"number\":8,\"time\":{\"millis\":1365004945521,\"text\":\"April 3, 2013\"},\"player\":1001,\"word\":{\"tiles\":[{\"number\":102,\"letter\":\"с\",\"cost\":2,\"wildcard\":false},{\"number\":30,\"letter\":\"е\",\"cost\":1,\"wildcard\":false},{\"number\":71,\"letter\":\"н\",\"cost\":1,\"wildcard\":false},{\"number\":82,\"letter\":\"о\",\"cost\":1,\"wildcard\":false}],\"position\":{\"row\":4,\"column\":9},\"direction\":\"VERTICAL\",\"text\":\"сено\"},\"points\":7,\"exchange\":0},{\"type\":\"MAKE\",\"number\":9,\"time\":{\"millis\":1365088043820,\"text\":\"April 4, 2013\"},\"player\":1003,\"word\":{\"tiles\":[{\"number\":67,\"letter\":\"м\",\"cost\":2,\"wildcard\":false},{\"number\":3,\"letter\":\"а\",\"cost\":1,\"wildcard\":false},{\"number\":129,\"letter\":\"я\",\"cost\":4,\"wildcard\":false},{\"number\":55,\"letter\":\"к\",\"cost\":2,\"wildcard\":false}],\"position\":{\"row\":9,\"column\":6},\"direction\":\"HORIZONTAL\",\"text\":\"маяк\"},\"points\":13,\"exchange\":0},{\"type\":\"MAKE\",\"number\":10,\"time\":{\"millis\":1365092221089,\"text\":\"April 4, 2013\"},\"player\":1001,\"word\":{\"tiles\":[{\"number\":24,\"letter\":\"д\",\"cost\":2,\"wildcard\":false},{\"number\":109,\"letter\":\"у\",\"cost\":3,\"wildcard\":false},{\"number\":113,\"letter\":\"х\",\"cost\":5,\"wildcard\":false}],\"position\":{\"row\":4,\"column\":6},\"direction\":\"HORIZONTAL\",\"text\":\"дух\"},\"points\":17,\"exchange\":0},{\"type\":\"MAKE\",\"number\":11,\"time\":{\"millis\":1365094176119,\"text\":\"April 4, 2013\"},\"player\":1003,\"word\":{\"tiles\":[{\"number\":73,\"letter\":\"н\",\"cost\":1,\"wildcard\":false},{\"number\":109,\"letter\":\"у\",\"cost\":3,\"wildcard\":false},{\"number\":18,\"letter\":\"г\",\"cost\":3,\"wildcard\":false},{\"number\":2,\"letter\":\"а\",\"cost\":1,\"wildcard\":false}],\"position\":{\"row\":3,\"column\":7},\"direction\":\"VERTICAL\",\"text\":\"нуга\"},\"points\":16,\"exchange\":0},{\"type\":\"MAKE\",\"number\":12,\"time\":{\"millis\":1365155457121,\"text\":\"April 5, 2013\"},\"player\":1001,\"word\":{\"tiles\":[{\"number\":13,\"letter\":\"в\",\"cost\":2,\"wildcard\":false},{\"number\":30,\"letter\":\"е\",\"cost\":1,\"wildcard\":false},{\"number\":74,\"letter\":\"н\",\"cost\":1,\"wildcard\":false},{\"number\":77,\"letter\":\"о\",\"cost\":1,\"wildcard\":false},{\"number\":58,\"letter\":\"к\",\"cost\":2,\"wildcard\":false}],\"position\":{\"row\":5,\"column\":8},\"direction\":\"HORIZONTAL\",\"text\":\"венок\"},\"points\":7,\"exchange\":0},{\"type\":\"MAKE\",\"number\":13,\"time\":{\"millis\":1365275104886,\"text\":\"April 6, 2013\"},\"player\":1003,\"word\":{\"tiles\":[{\"number\":58,\"letter\":\"к\",\"cost\":2,\"wildcard\":false},{\"number\":44,\"letter\":\"и\",\"cost\":1,\"wildcard\":false},{\"number\":50,\"letter\":\"й\",\"cost\":4,\"wildcard\":false}],\"position\":{\"row\":5,\"column\":12},\"direction\":\"HORIZONTAL\",\"text\":\"кий\"},\"points\":14,\"exchange\":0},{\"type\":\"MAKE\",\"number\":14,\"time\":{\"millis\":1365309030925,\"text\":\"April 7, 2013\"},\"player\":1001,\"word\":{\"tiles\":[{\"number\":38,\"letter\":\"з\",\"cost\":5,\"wildcard\":false},{\"number\":8,\"letter\":\"а\",\"cost\":1,\"wildcard\":false},{\"number\":93,\"letter\":\"р\",\"cost\":2,\"wildcard\":false},{\"number\":130,\"letter\":\"я\",\"cost\":4,\"wildcard\":false}],\"position\":{\"row\":7,\"column\":3},\"direction\":\"HORIZONTAL\",\"text\":\"заря\"},\"points\":24,\"exchange\":0},{\"type\":\"MAKE\",\"number\":15,\"time\":{\"millis\":1365338769112,\"text\":\"April 7, 2013\"},\"player\":1003,\"word\":{\"tiles\":[{\"number\":91,\"letter\":\"п\",\"cost\":2,\"wildcard\":false},{\"number\":26,\"letter\":\"е\",\"cost\":1,\"wildcard\":false},{\"number\":50,\"letter\":\"й\",\"cost\":4,\"wildcard\":false},{\"number\":37,\"letter\":\"з\",\"cost\":5,\"wildcard\":false},{\"number\":132,\"letter\":\"а\",\"cost\":0,\"wildcard\":true},{\"number\":36,\"letter\":\"ж\",\"cost\":5,\"wildcard\":false}],\"position\":{\"row\":3,\"column\":14},\"direction\":\"VERTICAL\",\"text\":\"пейзаж\"},\"points\":111,\"exchange\":0},{\"type\":\"MAKE\",\"number\":16,\"time\":{\"millis\":1365351944868,\"text\":\"April 7, 2013\"},\"player\":1001,\"word\":{\"tiles\":[{\"number\":55,\"letter\":\"к\",\"cost\":2,\"wildcard\":false},{\"number\":83,\"letter\":\"о\",\"cost\":1,\"wildcard\":false},{\"number\":133,\"letter\":\"ж\",\"cost\":0,\"wildcard\":true},{\"number\":111,\"letter\":\"у\",\"cost\":3,\"wildcard\":false},{\"number\":114,\"letter\":\"х\",\"cost\":5,\"wildcard\":false}],\"position\":{\"row\":9,\"column\":9},\"direction\":\"HORIZONTAL\",\"text\":\"кожух\"},\"points\":22,\"exchange\":0},{\"type\":\"MAKE\",\"number\":17,\"time\":{\"millis\":1365494844735,\"text\":\"April 9, 2013\"},\"player\":1003,\"word\":{\"tiles\":[{\"number\":36,\"letter\":\"ж\",\"cost\":5,\"wildcard\":false},{\"number\":28,\"letter\":\"е\",\"cost\":1,\"wildcard\":false},{\"number\":99,\"letter\":\"с\",\"cost\":2,\"wildcard\":false},{\"number\":104,\"letter\":\"т\",\"cost\":2,\"wildcard\":false}],\"position\":{\"row\":8,\"column\":14},\"direction\":\"VERTICAL\",\"text\":\"жест\"},\"points\":30,\"exchange\":0},{\"type\":\"MAKE\",\"number\":18,\"time\":{\"millis\":1365524678752,\"text\":\"April 9, 2013\"},\"player\":1001,\"word\":{\"tiles\":[{\"number\":126,\"letter\":\"э\",\"cost\":8,\"wildcard\":false},{\"number\":93,\"letter\":\"р\",\"cost\":2,\"wildcard\":false},{\"number\":9,\"letter\":\"а\",\"cost\":1,\"wildcard\":false}],\"position\":{\"row\":6,\"column\":5},\"direction\":\"VERTICAL\",\"text\":\"эра\"},\"points\":11,\"exchange\":0},{\"type\":\"MAKE\",\"number\":19,\"time\":{\"millis\":1365747594378,\"text\":\"April 12, 2013\"},\"player\":1003,\"word\":{\"tiles\":[{\"number\":23,\"letter\":\"д\",\"cost\":2,\"wildcard\":false},{\"number\":110,\"letter\":\"у\",\"cost\":3,\"wildcard\":false},{\"number\":20,\"letter\":\"г\",\"cost\":3,\"wildcard\":false},{\"number\":8,\"letter\":\"а\",\"cost\":1,\"wildcard\":false}],\"position\":{\"row\":4,\"column\":4},\"direction\":\"VERTICAL\",\"text\":\"дуга\"},\"points\":12,\"exchange\":0},{\"type\":\"MAKE\",\"number\":20,\"time\":{\"millis\":1365767573054,\"text\":\"April 12, 2013\"},\"player\":1001,\"word\":{\"tiles\":[{\"number\":115,\"letter\":\"ц\",\"cost\":5,\"wildcard\":false},{\"number\":16,\"letter\":\"в\",\"cost\":2,\"wildcard\":false},{\"number\":27,\"letter\":\"е\",\"cost\":1,\"wildcard\":false},{\"number\":104,\"letter\":\"т\",\"cost\":2,\"wildcard\":false}],\"position\":{\"row\":11,\"column\":11},\"direction\":\"HORIZONTAL\",\"text\":\"цвет\"},\"points\":20,\"exchange\":0},{\"type\":\"MAKE\",\"number\":21,\"time\":{\"millis\":1365794713074,\"text\":\"April 12, 2013\"},\"player\":1003,\"word\":{\"tiles\":[{\"number\":127,\"letter\":\"ю\",\"cost\":10,\"wildcard\":false},{\"number\":56,\"letter\":\"к\",\"cost\":2,\"wildcard\":false},{\"number\":57,\"letter\":\"к\",\"cost\":2,\"wildcard\":false},{\"number\":6,\"letter\":\"а\",\"cost\":1,\"wildcard\":false}],\"position\":{\"row\":10,\"column\":6},\"direction\":\"HORIZONTAL\",\"text\":\"юкка\"},\"points\":27,\"exchange\":0},{\"type\":\"MAKE\",\"number\":22,\"time\":{\"millis\":1365831655094,\"text\":\"April 13, 2013\"},\"player\":1001,\"word\":{\"tiles\":[{\"number\":115,\"letter\":\"ц\",\"cost\":5,\"wildcard\":false},{\"number\":34,\"letter\":\"е\",\"cost\":1,\"wildcard\":false},{\"number\":69,\"letter\":\"н\",\"cost\":1,\"wildcard\":false},{\"number\":106,\"letter\":\"т\",\"cost\":2,\"wildcard\":false}],\"position\":{\"row\":11,\"column\":11},\"direction\":\"VERTICAL\",\"text\":\"цент\"},\"points\":27,\"exchange\":0},{\"type\":\"MAKE\",\"number\":23,\"time\":{\"millis\":1365880670992,\"text\":\"April 13, 2013\"},\"player\":1003,\"word\":{\"tiles\":[{\"number\":11,\"letter\":\"б\",\"cost\":3,\"wildcard\":false},{\"number\":110,\"letter\":\"у\",\"cost\":3,\"wildcard\":false},{\"number\":52,\"letter\":\"й\",\"cost\":4,\"wildcard\":false}],\"position\":{\"row\":5,\"column\":3},\"direction\":\"HORIZONTAL\",\"text\":\"буй\"},\"points\":18,\"exchange\":0},{\"type\":\"MAKE\",\"number\":24,\"time\":{\"millis\":1365911468633,\"text\":\"April 14, 2013\"},\"player\":1001,\"word\":{\"tiles\":[{\"number\":14,\"letter\":\"в\",\"cost\":2,\"wildcard\":false},{\"number\":43,\"letter\":\"и\",\"cost\":1,\"wildcard\":false},{\"number\":69,\"letter\":\"н\",\"cost\":1,\"wildcard\":false},{\"number\":84,\"letter\":\"о\",\"cost\":1,\"wildcard\":false}],\"position\":{\"row\":13,\"column\":9},\"direction\":\"HORIZONTAL\",\"text\":\"вино\"},\"points\":12,\"exchange\":0},{\"type\":\"MAKE\",\"number\":25,\"time\":{\"millis\":1365963890997,\"text\":\"April 14, 2013\"},\"player\":1003,\"word\":{\"tiles\":[{\"number\":53,\"letter\":\"к\",\"cost\":2,\"wildcard\":false},{\"number\":7,\"letter\":\"а\",\"cost\":1,\"wildcard\":false},{\"number\":127,\"letter\":\"ю\",\"cost\":10,\"wildcard\":false},{\"number\":56,\"letter\":\"к\",\"cost\":2,\"wildcard\":false}],\"position\":{\"row\":10,\"column\":4},\"direction\":\"HORIZONTAL\",\"text\":\"каюк\"},\"points\":15,\"exchange\":0}]}");

					final ClientResponse r = new ClientResponse(true, board);
					//getWiseMatchesClient().post("/playground/scribble/board/load.ajax?b=" + boardId);
					if (r.isSuccess()) {
						return JSONScribbleParser.parseGame(r.getData());
					} else {
						Log.e("wm", "Board can't be loaded from server");
						return null;
					}
				} catch (JSONException ex) {//| CooperationException | CommunicationException ex) {
					Log.e("wm", ex.getClass().getName() + ": " + ex.getMessage());
					return null;
				}
			}

			@Override
			protected void onPostExecute(ScribbleGame board) {
				super.onPostExecute(board);
				Log.i("wm", "asd: " + board);
				showBoardInfo(board);
			}
		};
		a.execute(boardId);
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

	private void showBoardInfo(ScribbleGame board) {
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(board.getSettings().getTitle() + " #" + board.getBoardId());

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
		for (ScribblePlayer player : board.getPlayers()) {
			final TableRow row = (TableRow) tableLayout.getChildAt(index);

			final PlayerView pv = (PlayerView) row.getChildAt(0);
			pv.setPlayer(player);

			final TextView view = (TextView) row.getChildAt(1);
			view.setText(String.valueOf(player.getPoints()));

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
}