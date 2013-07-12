package wisematches.client.android.app.playground.model;

import wisematches.client.android.data.model.person.Personality;
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

	ScribbleBank getScribbleBank();


	Personality getPlayerTurn();

	List<Personality> getPlayers();

	Personality getPlayer(long player);


	ScribbleTile[] getHandTiles();

	List<ScribbleMove> getScribbleMoves();


	void resign();

	void passTurn();

	void makeTurn(ScribbleWord word);

	void exchange(Set<ScribbleTile> tiles);


	ScribbleWord getSelectedWord();


	void selectWord(ScribbleWord word);


	boolean isActive();

	int getBoardTilesCount();
}
