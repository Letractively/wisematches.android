package wisematches.client.android.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import wisematches.client.android.app.playground.scribble.ActiveGamesActivity;
import wisematches.client.android.app.playground.scribble.CreateGameActivity;
import wisematches.client.android.app.playground.scribble.FinishedGamesActivity;
import wisematches.client.android.app.playground.scribble.JoinGameActivity;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class MenuFactory {
	private MenuFactory() {
	}

	public static MenuItem addMenuItem(Menu menu, int groupId, int order, Type type, int actionEnum) {
		MenuItem item = menu.add(groupId, type.ordinal(), order, type.name);
		if (type.icon != 0) {
			item.setIcon(type.icon);
		}
		item.setShowAsAction(actionEnum);
		return item;
	}

	public static boolean startMenuActivity(Activity activity, MenuItem item) {
		for (Type type : Type.values()) {
			if (type.ordinal() == item.getItemId()) {
				activity.startActivity(type.createIntent(activity));
				return true;
			}
		}
		return false;
	}

	public static enum Type {
		ACTIVE_GAMES("Текущие игры", 0) {
			@Override
			public Intent createIntent(Context context) {
				return ActiveGamesActivity.createIntent(context);
			}
		},
		FINISHED_GAMES("Завершенные игры", android.R.drawable.ic_menu_recent_history) {
			@Override
			public Intent createIntent(Context context) {
				return FinishedGamesActivity.createIntent(context);
			}
		},

		JOIN_GAME("Присоединиться", android.R.drawable.ic_menu_more) {
			@Override
			public Intent createIntent(Context context) {
				return JoinGameActivity.createIntent(context);
			}
		},
		CREATE_GAME("Новая игра", android.R.drawable.ic_menu_add) {
			@Override
			public Intent createIntent(Context context) {
				return CreateGameActivity.createIntent(context);
			}
		};

		private final int icon;
		private final String name;

		private Type(String name, int icon) {
			this.icon = icon;
			this.name = name;
		}

		public int getIcon() {
			return icon;
		}

		public String getName() {
			return name;
		}

		public abstract Intent createIntent(Context context);
	}
}