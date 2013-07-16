package wisematches.client.android.data.model.scribble;

import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface ScribbleController {
	void resign(BoardValidator validator);

	void passTurn(BoardValidator validator);

	void makeTurn(ScribbleWord word, BoardValidator validator);

	void exchange(Set<ScribbleTile> tiles, BoardValidator validator);
}
