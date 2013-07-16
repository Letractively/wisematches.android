package wisematches.client.android.data.model.scribble;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface SelectionListener {
	void onSelectionChanged(ScribbleWord word, ScribbleTile[] tiles);
}
