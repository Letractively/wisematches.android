package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;
import wisematches.client.android.data.model.person.Personality;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleHand implements Parcelable {
	private int points;
	private int oldRating;
	private int newRating;
	private boolean winner;
	private final Personality personality;

	public ScribbleHand(Personality p, int points, int oldRating, int newRating, boolean winner) {
		this.personality = p;
		this.points = points;
		this.oldRating = oldRating;
		this.newRating = newRating;
		this.winner = winner;
	}

	public ScribbleHand(Parcel in) {
		personality = in.readParcelable(getClass().getClassLoader());
		this.points = in.readInt();
		this.oldRating = in.readInt();
		this.newRating = in.readInt();
		this.winner = in.readByte() == 1;
	}

	public Personality getPersonality() {
		return personality;
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

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(personality, flags);
		dest.writeInt(points);
		dest.writeInt(oldRating);
		dest.writeInt(newRating);
		dest.writeByte((byte) (winner ? 1 : 0));
	}

	public static final Parcelable.Creator<ScribbleHand> CREATOR = new Parcelable.Creator<ScribbleHand>() {
		public ScribbleHand createFromParcel(Parcel in) {
			return new ScribbleHand(in);
		}

		public ScribbleHand[] newArray(int size) {
			return new ScribbleHand[size];
		}
	};
}
