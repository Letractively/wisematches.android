package wisematches.client.android.app.playground;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import wisematches.client.android.R;
import wisematches.client.android.app.playground.activity.ActiveGamesActivity;
import wisematches.client.android.app.playground.activity.CreateGameActivity;
import wisematches.client.android.app.playground.activity.WaitingGamesActivity;

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
		Intent intent = null;
		if (item.getItemId() == android.R.id.home) {
			intent = Type.HOME.createIntent(activity);
		} else {
			Type[] values = Type.values();
			for (int i = 0; i < values.length && intent == null; i++) {
				Type type = values[i];
				if (type.ordinal() == item.getItemId()) {
					intent = type.createIntent(activity);
				}
			}
		}

		if (intent == null || intent.getComponent().equals(activity.getComponentName())) {
			return false;
		}
		activity.startActivity(intent);
		return true;
	}

	public static enum Type {
		HOME("Текущие игры", R.drawable.ic_logo) {
			@Override
			public Intent createIntent(Context context) {
				return ActiveGamesActivity.createIntent(context);
			}
		},

		JOIN_GAME("Присоединиться", R.drawable.ic_menu_join) {
			@Override
			public Intent createIntent(Context context) {
				return WaitingGamesActivity.createIntent(context);
			}
		},
		CREATE_GAME("Новая игра", R.drawable.ic_menu_create) {
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

		public abstract Intent createIntent(Context context);
	}
}