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
	private static int globalItemIDS = 1;

	private MenuFactory() {
	}

	public static MenuItem addMenuItem(Menu menu, int groupId, int order, Type type, Visibility visibility) {
		MenuItem item = menu.add(groupId, type.ordinal(), order, type.name);
		if (type.icon != 0) {
			item.setIcon(type.icon);
		}
		if (visibility != null) {
			item.setShowAsAction(visibility.getActionView());
		}
		return item;
	}

	public static boolean startMenuActivity(Activity activity, MenuItem item) {
		final Type type = Type.byItemId(item);
		if (type == null) {
			return false;
		}

		final Intent intent = type.createIntent(activity);
		if (intent == null || intent.getComponent().equals(activity.getComponentName())) {
			return false;
		}
		activity.startActivity(intent);
		return true;
	}

	public static boolean createSystemMenu(Menu menu) {
//		addMenuItem(menu, 100, 100, Type.EXIT, Visibility.NEVER);
		return false;
	}

	public static enum Type {
		HOME("Текущие игры", R.drawable.ic_logo, android.R.id.home) {
			@Override
			public Intent createIntent(Context context) {
				return ActiveGamesActivity.createIntent(context);
			}
		},
		EXIT("Выйти", android.R.drawable.ic_menu_close_clear_cancel) {
			@Override
			public Intent createIntent(Context context) {
				return null;
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
		},
		MOVES_HISTORY("История Ходов", android.R.drawable.ic_menu_recent_history) {
			@Override
			public Intent createIntent(Context context) {
				return null;
			}
		};

		private final int icon;
		private final int itemId;
		private final String name;

		private Type(String name, int icon) {
			this(name, icon, globalItemIDS++);
		}

		private Type(String name, int icon, int itemId) {
			this.icon = icon;
			this.name = name;
			this.itemId = itemId;
		}

		public abstract Intent createIntent(Context context);

		public boolean is(MenuItem item) {
			return itemId == item.getItemId();
		}

		public static Type byItemId(MenuItem item) {
			for (Type type : values()) {
				if (type.is(item)) {
					return type;
				}
			}
			return null;
		}
	}

	/**
	 * @author Sergey Klimenko (smklimenko@gmail.com)
	 */
	public static enum Visibility {
		NEVER(MenuItem.SHOW_AS_ACTION_NEVER),
		ALWAYS(MenuItem.SHOW_AS_ACTION_ALWAYS),
		IF_ROOM(MenuItem.SHOW_AS_ACTION_IF_ROOM),
		WITH_TEXT(MenuItem.SHOW_AS_ACTION_WITH_TEXT),
		COLLAPSE(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW),;

		private final int actionView;

		Visibility(int actionView) {
			this.actionView = actionView;
		}

		public int getActionView() {
			return actionView;
		}
	}
}