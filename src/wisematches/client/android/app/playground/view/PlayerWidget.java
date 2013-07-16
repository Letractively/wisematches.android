package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableLayout;
import wisematches.client.android.R;
import wisematches.client.android.data.model.scribble.BoardStateListener;
import wisematches.client.android.data.model.scribble.ScribbleBoard;
import wisematches.client.android.data.model.scribble.ScribbleHand;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerWidget extends AbstractBoardWidget {
	private ScribbleBoard scribbleBoard;

	private final Map<ScribbleHand, PlayerView> playerViewMap = new HashMap<>();
	private final TheBoardStateListener boardStateListener = new TheBoardStateListener();

	public PlayerWidget(Context context, AttributeSet attrs) {
		super(context, attrs, R.layout.playground_board_widget_players, "Игроки");
	}

	@Override
	public void boardInitialized(ScribbleBoard board) {
		scribbleBoard = board;
		scribbleBoard.addBoardStateListener(boardStateListener);

		initPlayers(scribbleBoard);
	}

	@Override
	public void boardTerminated(ScribbleBoard board) {
		scribbleBoard.removeBoardStateListener(boardStateListener);
		scribbleBoard = null;
	}

	private void initPlayers(ScribbleBoard scribbleBoard) {
		final TableLayout viewById = (TableLayout) findViewById(R.id.scribbleBoardPlayers);

		final ScribbleHand[] players = scribbleBoard.getPlayers();
		for (ScribbleHand player : players) {
			final PlayerView playerView = new PlayerView(getContext(), player);
			playerViewMap.put(player, playerView);

			viewById.addView(playerView.getInflate());
		}
	}

	private class TheBoardStateListener implements BoardStateListener {
		@Override
		public void gameStateChanged() {
			for (PlayerView playerView : playerViewMap.values()) {
				playerView.validate();
			}
		}

		@Override
		public void gameStateValidated() {
			for (PlayerView playerView : playerViewMap.values()) {
				playerView.validate();
			}
		}
	}
}
