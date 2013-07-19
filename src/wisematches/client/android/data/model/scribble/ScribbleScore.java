package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleScore implements Parcelable {
	private int points;
	private int oldRating;
	private int newRating;
	private boolean winner;

	public ScribbleScore(int points, int oldRating, int newRating, boolean winner) {
		this.points = points;
		this.oldRating = oldRating;
		this.newRating = newRating;
		this.winner = winner;
	}

	public ScribbleScore(Parcel in) {
		this.points = in.readInt();
		this.oldRating = in.readInt();
		this.newRating = in.readInt();
		this.winner = in.readByte() == 1;
	}

	public int getPoints() {
		return points;
	}

	public int getOldRating() {
		return oldRating;
	}

	public int getNewRating() {
		return newRating;
	}

	public boolean isWinner() {
		return winner;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public void validate(ScribbleScore score) {
		this.points = score.points;
		this.oldRating = score.oldRating;
		this.newRating = score.newRating;
		this.winner = score.winner;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(points);
		dest.writeInt(oldRating);
		dest.writeInt(newRating);
		dest.writeByte((byte) (winner ? 1 : 0));
	}

	public static final Parcelable.Creator<ScribbleScore> CREATOR = new Parcelable.Creator<ScribbleScore>() {
		public ScribbleScore createFromParcel(Parcel in) {
			return new ScribbleScore(in);
		}

		public ScribbleScore[] newArray(int size) {
			return new ScribbleScore[size];
		}
	};
}
