package wisematches.client.android.app.playground.model;

import wisematches.client.android.data.model.scribble.ScribbleMove;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameMoveListener {
	void onMoveDone(ScribbleMove move);
}
