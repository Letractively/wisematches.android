package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleTile implements Parcelable {
	private final int cost;
	private final int number;
	private final String letter;

	public ScribbleTile(int cost, int number, String letter) {
		this.cost = cost;
		this.number = number;
		this.letter = letter.toUpperCase();
	}

	public ScribbleTile(Parcel in) {
		this.cost = in.readInt();
		this.number = in.readInt();
		this.letter = in.readString();
	}

	public int getCost() {
		return cost;
	}

	public int getNumber() {
		return number;
	}

	public String getLetter() {
		return letter;
	}

	public boolean isWildcard() {
		return cost == 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ScribbleTile)) return false;

		ScribbleTile that = (ScribbleTile) o;
		return number == that.number;
	}

	@Override
	public int hashCode() {
		return number;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ScribbleTile{");
		sb.append("cost=").append(cost);
		sb.append(", number=").append(number);
		sb.append(", letter='").append(letter).append('\'');
		sb.append('}');
		return sb.toString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(cost);
		dest.writeInt(number);
		dest.writeString(letter);
	}

	public static final Parcelable.Creator<ScribbleTile> CREATOR = new Parcelable.Creator<ScribbleTile>() {
		public ScribbleTile createFromParcel(Parcel in) {
			return new ScribbleTile(in);
		}

		public ScribbleTile[] newArray(int size) {
			return new ScribbleTile[size];
		}
	};
}
