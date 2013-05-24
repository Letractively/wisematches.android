package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleWord implements Parcelable {
	private final int row;
	private final int column;
	private final WordDirection direction;
	private final ScribbleTile[] selectedTiles;

	public ScribbleWord(int row, int column, WordDirection direction, ScribbleTile[] selectedTiles) {
		this.row = row;
		this.column = column;
		this.direction = direction;
		this.selectedTiles = selectedTiles;
	}

	public ScribbleWord(Parcel in) {
		this.row = in.readInt();
		this.column = in.readInt();
		this.direction = WordDirection.values()[in.readInt()];
		this.selectedTiles = (ScribbleTile[]) in.readParcelableArray(getClass().getClassLoader());
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

	public ScribbleTile[] getSelectedTiles() {
		return selectedTiles;
	}

	public CharSequence getText() {
		StringBuilder b = new StringBuilder();
		for (ScribbleTile selectedTile : selectedTiles) {
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
		dest.writeParcelableArray(selectedTiles, flags);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ScribbleWord{");
		sb.append("row=").append(row);
		sb.append(", column=").append(column);
		sb.append(", direction=").append(direction);
		sb.append(", selectedTiles=").append(Arrays.toString(selectedTiles));
		sb.append('}');
		return sb.toString();
	}

	public static final Parcelable.Creator<ScribbleWord> CREATOR = new Parcelable.Creator<ScribbleWord>() {
		public ScribbleWord createFromParcel(Parcel in) {
			return new ScribbleWord(in);
		}

		public ScribbleWord[] newArray(int size) {
			return new ScribbleWord[size];
		}
	};
}
