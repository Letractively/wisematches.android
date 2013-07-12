package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.util.AttributeSet;
import wisematches.client.android.R;
import wisematches.client.android.app.playground.model.ScribbleController;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayersWidget extends AbstractBoardWidget {
	public PlayersWidget(Context context, AttributeSet attrs) {
		super(context, attrs, R.layout.playground_board_widget_players, "Игроки");
	}

	@Override
	public void controllerInitialized(ScribbleController controller) {
	}

	@Override
	public void controllerTerminated(ScribbleController controller) {
	}
}
