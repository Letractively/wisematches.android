package wisematches.client.android.app.playground.scribble;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesActivity;
import wisematches.client.android.app.MenuFactory;
import wisematches.client.android.app.playground.scribble.board.ScribbleBoardActivity;
import wisematches.client.android.data.DataRequestManager;
import wisematches.client.android.data.model.Id;
import wisematches.client.android.data.model.Language;
import wisematches.client.android.widget.ArrayAdapter;
import wisematches.client.android.widget.LanguageAdapter;

import java.util.Arrays;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class CreateGameActivity extends WiseMatchesActivity implements ActionBar.TabListener {
	private EditText titleEditor;
	private Spinner languageEditor;
	private Spinner timeoutEditor;

	private RadioGroup robotsGroup;
	private RadioGroup waitingGroup;

	private Button createButton;

	private static final int[] TIMEOUTS = new int[]{2, 3, 4, 5, 7, 10, 14};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playground_create_game);

		titleEditor = (EditText) findViewById(R.id.playgroundCreateTitle);
		languageEditor = (Spinner) findViewById(R.id.playgroundCreateLanguage);
		timeoutEditor = (Spinner) findViewById(R.id.playgroundCreateTimeout);

		robotsGroup = (RadioGroup) findViewById(R.id.playgroundCreateRobot);
		waitingGroup = (RadioGroup) findViewById(R.id.playgroundCreateWaiting);

		createButton = (Button) findViewById(R.id.playgroundCreateBtn);

		final ActionBar supportActionBar = getSupportActionBar();
		supportActionBar.setTitle("Новая Игра");
		supportActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		final ActionBar.Tab robotsTab = supportActionBar.newTab();
		robotsTab.setText("Робот");
		robotsTab.setTabListener(this);
		robotsTab.setTag(R.id.playgroundCreateRobot);
		supportActionBar.addTab(robotsTab, true);

		final ActionBar.Tab waitingTab = supportActionBar.newTab();
		waitingTab.setText("Ожидать");
		waitingTab.setTag(R.id.playgroundCreateWaiting);
		waitingTab.setTabListener(this);
		supportActionBar.addTab(waitingTab);

		languageEditor.setAdapter(new LanguageAdapter(this, android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item));
		timeoutEditor.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item,
				true, Arrays.asList("2 дня", "3 дня", "4 дня", "5 дней", "7 дней", "10 дней", "14 дней")));

		createButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createNewGame();
			}
		});
	}

	private void createNewGame() {
		createButton.setEnabled(false);

		String createTab = null;
		String robotType = null;
		int opponentsCount = 0;

		final int tag = (int) getSupportActionBar().getSelectedTab().getTag();
		if (tag == R.id.playgroundCreateRobot) {
			createTab = "ROBOT";

			switch (robotsGroup.getCheckedRadioButtonId()) {
				case R.id.playgroundCreateRobotDull:
					robotType = "DULL";
					break;
				case R.id.playgroundCreateRobotTrainee:
					robotType = "TRAINEE";
					break;
				case R.id.playgroundCreateRobotExpert:
					robotType = "EXPERT";
					break;
			}
		} else if (tag == R.id.playgroundCreateWaiting) {
			createTab = "WAIT";
			switch (waitingGroup.getCheckedRadioButtonId()) {
				case R.id.playgroundCreateWaitingOne:
					opponentsCount = 1;
					break;
				case R.id.playgroundCreateWaitingTwo:
					opponentsCount = 2;
					break;
				case R.id.playgroundCreateWaitingThree:
					opponentsCount = 3;
					break;
			}
		}

		getRequestManager().createNewGame(titleEditor.getText().toString(), (Language) languageEditor.getSelectedItem(),
				TIMEOUTS[timeoutEditor.getSelectedItemPosition()], createTab, robotType, opponentsCount, new DataRequestManager.DataResponse<Id>() {
			@Override
			public void onSuccess(Id data) {
				startActivity(ScribbleBoardActivity.createIntent(CreateGameActivity.this, data.getId()));
			}

			@Override
			public void onFailure(String code, String message) {
				createButton.setEnabled(true);
			}

			@Override
			public void onDataError() {
				createButton.setEnabled(true);
			}

			@Override
			public void onConnectionError(int code) {
				createButton.setEnabled(true);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuFactory.addMenuItem(menu, 1, 1, MenuFactory.Type.ACTIVE_GAMES, MenuItem.SHOW_AS_ACTION_IF_ROOM);
		MenuFactory.addMenuItem(menu, 1, 2, MenuFactory.Type.JOIN_GAME, MenuItem.SHOW_AS_ACTION_IF_ROOM);
		MenuFactory.addMenuItem(menu, 2, 3, MenuFactory.Type.FINISHED_GAMES, MenuItem.SHOW_AS_ACTION_NEVER);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuFactory.startMenuActivity(this, item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		findViewById((Integer) tab.getTag()).setVisibility(View.VISIBLE);
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
		findViewById((Integer) tab.getTag()).setVisibility(View.GONE);
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	}

	public static Intent createIntent(Context context) {
		return new Intent(context, CreateGameActivity.class);
	}
}
