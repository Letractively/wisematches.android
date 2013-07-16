package wisematches.client.android.data.model.scribble;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SelectionModel {
	private ScribbleWord selectedWord = null;
	private ScribbleTile[] selectedTiles = null;

	private final List<SelectionListener> listeners = new ArrayList<>();

	public SelectionModel() {
	}

	public void addSelectionListener(SelectionListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}

	public void removeSelectionListener(SelectionListener l) {
		if (l != null) {
			listeners.remove(l);
		}
	}

	public ScribbleWord getSelectedWord() {
		return selectedWord;
	}

	public ScribbleTile[] getSelectedTiles() {
		return selectedTiles;
	}

	public void setSelection(ScribbleWord word) {
		this.selectedWord = word;
		this.selectedTiles = word.getTiles();

		notifySelectionChanged();
	}

	private void notifySelectionChanged() {
		for (SelectionListener listener : listeners) {
			listener.onSelectionChanged(selectedWord, selectedTiles);
		}
	}

	public void setSelection(Set<ScribbleTile> tiles) {
		this.selectedWord = null;
		this.selectedTiles = tiles.toArray(new ScribbleTile[tiles.size()]);

		notifySelectionChanged();
	}

	public void clearSelection() {
		this.selectedWord = null;
		this.selectedTiles = null;

		notifySelectionChanged();
	}
}
