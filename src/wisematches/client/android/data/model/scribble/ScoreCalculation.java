package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScoreCalculation implements Parcelable {
	private final short points;
	private final boolean allFromHand;
	private final ScoreBonus.Type[] bonuses;
	private final String formula;

	public ScoreCalculation(short points, boolean allFromHand, ScoreBonus.Type[] bonuses, String formula) {
		this.points = points;
		this.allFromHand = allFromHand;
		this.bonuses = bonuses;
		this.formula = formula;
	}

	public ScoreCalculation(Parcel in) {
		this.points = (short) in.readInt();
		this.allFromHand = in.readByte() == 1;

		final int[] intArray = in.createIntArray();
		bonuses = new ScoreBonus.Type[intArray.length];
		for (int i = 0; i < intArray.length; i++) {
			int i1 = intArray[i];
			if (i1 != -1) {
				bonuses[i] = ScoreBonus.Type.values()[i1];
			}
		}
		this.formula = in.readString();
	}

	public short getPoints() {
		return points;
	}

	public String getFormula() {
		return formula;
	}

	public boolean isAllFromHand() {
		return allFromHand;
	}

	public ScoreBonus.Type[] getBonuses() {
		return bonuses;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.points);
		dest.writeByte((byte) (this.allFromHand ? 1 : 0));

		final int[] ids = new int[bonuses.length];
		for (int i = 0; i < bonuses.length; i++) {
			ScoreBonus.Type bonuse = bonuses[i];
			ids[i] = bonuse == null ? -1 : bonuse.ordinal();
		}
		dest.writeIntArray(ids);
		dest.writeString(this.formula);
	}

	public static final Parcelable.Creator<ScoreCalculation> CREATOR = new Parcelable.Creator<ScoreCalculation>() {
		public ScoreCalculation createFromParcel(Parcel in) {
			return new ScoreCalculation(in);
		}

		public ScoreCalculation[] newArray(int size) {
			return new ScoreCalculation[size];
		}
	};
}
