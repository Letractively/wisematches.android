package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScoreEngine implements Parcelable {
	private final int allHandBonus;
	private final ScoreBonus[] scoreBonuses;

	private final ScoreBonus[][] bonusesMatrix = new ScoreBonus[8][8];

	public ScoreEngine(ScoreBonus[] scoreBonuses, int allHandBonus) {
		this.allHandBonus = allHandBonus;
		this.scoreBonuses = scoreBonuses;

		for (ScoreBonus scoreBonus : scoreBonuses) {
			bonusesMatrix[scoreBonus.getRow()][scoreBonus.getColumn()] = scoreBonus;
		}
	}

	public ScoreEngine(Parcel in) {
		this.allHandBonus = in.readInt();
		this.scoreBonuses = (ScoreBonus[]) in.readParcelableArray(getClass().getClassLoader());

		for (ScoreBonus scoreBonus : scoreBonuses) {
			bonusesMatrix[scoreBonus.getRow()][scoreBonus.getColumn()] = scoreBonus;
		}
	}

	public int getAllHandBonus() {
		return allHandBonus;
	}

	public ScoreBonus[] getScoreBonuses() {
		return scoreBonuses;
	}

	public ScoreBonus getScoreBonus(int row, int col) {
		return bonusesMatrix[row < 8 ? row : 14 - row][col < 8 ? col : 14 - col];
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(allHandBonus);
		dest.writeParcelableArray(scoreBonuses, flags);
	}

	public static final Parcelable.Creator<ScoreEngine> CREATOR = new Parcelable.Creator<ScoreEngine>() {
		public ScoreEngine createFromParcel(Parcel in) {
			return new ScoreEngine(in);
		}

		public ScoreEngine[] newArray(int size) {
			return new ScoreEngine[size];
		}
	};
}
