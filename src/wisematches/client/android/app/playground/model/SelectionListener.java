package wisematches.client.android.app.playground.model;

import wisematches.client.android.data.model.scribble.ScoreCalculation;
import wisematches.client.android.data.model.scribble.ScribbleWord;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface SelectionListener {
	void onSelectionChanged(ScribbleWord word, ScoreCalculation score);
}
