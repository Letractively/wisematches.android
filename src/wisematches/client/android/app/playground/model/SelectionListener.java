package wisematches.client.android.app.playground.model;

import wisematches.client.android.data.model.scribble.ScribbleTile;
import wisematches.client.android.data.model.scribble.ScribbleWord;

import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface SelectionListener {
	void onWordSelected(ScribbleWord word);

	void onTileSelected(ScribbleTile tile, boolean selected, Set<ScribbleTile> selectedTiles);
}
