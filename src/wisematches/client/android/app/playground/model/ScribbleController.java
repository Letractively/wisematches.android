package wisematches.client.android.app.playground.model;

import wisematches.client.android.data.model.scribble.ScribbleBoard;
import wisematches.client.android.data.model.scribble.ScribbleTile;
import wisematches.client.android.data.model.scribble.ScribbleWord;

import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface ScribbleController {
	void addSelectionListener(SelectionListener l);

	void removeSelectionListener(SelectionListener l);

	void addGameMoveListener(GameMoveListener l);

	void removeGameMoveListener(GameMoveListener l);


	void resign();

	void passTurn();

	void makeTurn(ScribbleWord word);

	void exchange(Set<ScribbleTile> tiles);


	ScribbleWord getSelectedWord();

	void selectWord(ScribbleWord word);


	ScribbleBoard getScribbleBoard();
}
