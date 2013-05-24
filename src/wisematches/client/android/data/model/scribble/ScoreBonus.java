package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class ScoreBonus implements Parcelable {
	private final int row;
	private final int column;
	private final Type type;

	public ScoreBonus(int row, int column, Type type) {
		this.row = row;
		this.column = column;
		this.type = type;
	}

	public ScoreBonus(Parcel in) {
		this.row = in.readInt();
		this.column = in.readInt();
		this.type = Type.values()[in.readInt()];
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public Type getType() {
		return type;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(row);
		dest.writeInt(column);
		dest.writeInt(type.ordinal());
	}

	public static enum Type {
		L2(0xff9cff00),
		L3(0xff17e421),
		W2(0xff00fcff),
		W3(0xfffffc00);

		private final int color;

		private Type(int color) {
			this.color = color;
		}

		public int getColor() {
			return color;
		}
	}

	public static final Parcelable.Creator<ScoreBonus> CREATOR = new Parcelable.Creator<ScoreBonus>() {
		public ScoreBonus createFromParcel(Parcel in) {
			return new ScoreBonus(in);
		}

		public ScoreBonus[] newArray(int size) {
			return new ScoreBonus[size];
		}
	};
}
