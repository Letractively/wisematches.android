package wisematches.client.android.app.playground.scribble;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import wisematches.client.android.R;
import wisematches.client.android.WiseMatchesActivity;
import wisematches.client.android.app.MenuFactory;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class CreateGameActivity extends WiseMatchesActivity implements ActionBar.TabListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_create_game);

		final ActionBar supportActionBar = getSupportActionBar();
		supportActionBar.setTitle("Новая Игра");
		supportActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		final ActionBar.Tab robotsTab = supportActionBar.newTab();
		robotsTab.setText("Робот");
		robotsTab.setTabListener(this);
		supportActionBar.addTab(robotsTab, true);

		final ActionBar.Tab waitingTab = supportActionBar.newTab();
		waitingTab.setText("Ожидать");
		waitingTab.setTabListener(this);
		supportActionBar.addTab(waitingTab);
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
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	}

	public static Intent createIntent(Context context) {
		return new Intent(context, CreateGameActivity.class);
	}
}
