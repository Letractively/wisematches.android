package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleWord implements Parcelable, Iterable<ScribbleWord.IteratorItem> {
	private final int row;
	private final int column;
	private final ScribbleTile[] tiles;
	private final WordDirection direction;

	public ScribbleWord(int row, int column, WordDirection direction, ScribbleTile[] tiles) {
		this.row = row;
		this.column = column;
		this.direction = direction;
		this.tiles = tiles;
	}

	public ScribbleWord(Parcel in) {
		this.row = in.readInt();
		this.column = in.readInt();
		this.direction = WordDirection.values()[in.readInt()];
		this.tiles = in.createTypedArray(ScribbleTile.CREATOR);
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public WordDirection getDirection() {
		return direction;
	}

	public ScribbleTile[] getTiles() {
		return tiles;
	}

	public int length() {
		return tiles.length;
	}

	public CharSequence getText() {
		StringBuilder b = new StringBuilder();
		for (ScribbleTile selectedTile : tiles) {
			b.append(selectedTile.getLetter());
		}
		return b;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(row);
		dest.writeInt(column);
		dest.writeInt(direction.ordinal());
		dest.writeTypedArray(tiles, flags);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ScribbleWord{");
		sb.append("row=").append(row);
		sb.append(", column=").append(column);
		sb.append(", direction=").append(direction);
		sb.append(", tiles=").append(Arrays.toString(tiles));
		sb.append('}');
		return sb.toString();
	}

	public Iterator<IteratorItem> iterator() {
		return new Iterator<IteratorItem>() {
			private IteratorItem wordItem = new IteratorItem();

			private int index = 0;
			private int row = ScribbleWord.this.row;
			private int column = ScribbleWord.this.column;

			public boolean hasNext() {
				return index < tiles.length;
			}

			public IteratorItem next() {
				if (!hasNext()) {
					throw new IllegalStateException("Iterator has no next element");
				}

				wordItem.tile = tiles[index++];
				wordItem.row = row;
				wordItem.column = column;

				if (direction == WordDirection.VERTICAL) {
					row++;
				} else {
					column++;
				}
				return wordItem;
			}

			public void remove() {
				throw new UnsupportedOperationException("This is read-only iterator");
			}
		};
	}

	public static final Parcelable.Creator<ScribbleWord> CREATOR = new Parcelable.Creator<ScribbleWord>() {
		public ScribbleWord createFromParcel(Parcel in) {
			return new ScribbleWord(in);
		}

		public ScribbleWord[] newArray(int size) {
			return new ScribbleWord[size];
		}
	};

	/**
	 * A item of word's iterator. Each item has a tile, it's row and it's column position.
	 * <p/>
	 * Original iterator item is mutable and changed by iterator. You can
	 * use a <code>clone</code> method to create a clone of item.
	 */
	public static class IteratorItem {
		private ScribbleTile tile;
		private int row;
		private int column;

		private IteratorItem() {
		}

		private IteratorItem(ScribbleTile tile, int row, int column) {
			this.tile = tile;
			this.row = row;
			this.column = column;
		}

		/**
		 * Returns tile that represented by this item.
		 *
		 * @return the tile.
		 */
		public ScribbleTile getTile() {
			return tile;
		}

		/**
		 * Returns row position of the tile.
		 *
		 * @return the row position of the tile.
		 */
		public int getRow() {
			return row;
		}

		/**
		 * Returns column position of the tile.
		 *
		 * @return the column position of the tile.
		 */
		public int getColumn() {
			return column;
		}

		/**
		 * Creates clone of this item. Creates clone contains the same tile object.
		 *
		 * @return the clone of this item.
		 */
		public IteratorItem copy() {
			return new IteratorItem(tile, row, column);
		}
	}
}
