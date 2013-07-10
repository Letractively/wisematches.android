package wisematches.client.android.app.playground.model;

import wisematches.client.android.data.model.scribble.*;

import java.util.List;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface ScribbleController {
	void addSelectionListener(SelectionListener l);

	void removeSelectionListener(SelectionListener l);


	ScoreEngine getScoreEngine();

	ScribbleSettings getSettings();

	List<ScribbleMove> getScribbleMoves();


	void resign();

	void passTurn();

	void makeTurn(ScribbleWord word);

	void exchange(Set<ScribbleTile> tiles);


	ScribbleWord getSelectedWord();

	void setSelectWord(ScribbleWord word);


	Set<ScribbleTile> getSelectedTiles();

	void setSelectedTiles(Set<ScribbleTile> tiles);


	void clearSelection();
}
