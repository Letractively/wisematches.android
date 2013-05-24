package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleLetter implements Parcelable {
	private final int cost;
	private final int count;
	private final char letter;

	public ScribbleLetter(char letter, int count, int cost) {
		this.cost = cost;
		this.count = count;
		this.letter = letter;
	}

	public ScribbleLetter(Parcel in) {
		this.cost = in.readInt();
		this.count = in.readInt();
		this.letter = in.readString().charAt(0);
	}

	public int getCost() {
		return cost;
	}

	public int getCount() {
		return count;
	}

	public char getLetter() {
		return letter;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(cost);
		dest.writeInt(count);
		dest.writeString(String.valueOf(letter));
	}

	public static final Parcelable.Creator<ScribbleLetter> CREATOR = new Parcelable.Creator<ScribbleLetter>() {
		public ScribbleLetter createFromParcel(Parcel in) {
			return new ScribbleLetter(in);
		}

		public ScribbleLetter[] newArray(int size) {
			return new ScribbleLetter[size];
		}
	};
}
