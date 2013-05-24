package wisematches.client.android.app.playground.scribble.board;

import wisematches.client.android.data.model.scribble.ScribbleTile;
import wisematches.client.android.data.model.scribble.ScribbleWord;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface BoardViewListener {
	void onWordSelected(ScribbleWord word);

	void onTileSelected(ScribbleTile tile, boolean selected);
}
