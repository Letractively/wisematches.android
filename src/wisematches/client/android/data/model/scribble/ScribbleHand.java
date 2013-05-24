package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;
import wisematches.client.android.data.model.person.Personality;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleHand implements Parcelable {
	private final Personality player;
	private final ScribbleScore scores;

	public ScribbleHand(Personality player, ScribbleScore scores) {
		this.player = player;
		this.scores = scores;
	}

	public ScribbleHand(Parcel in) {
		player = in.readParcelable(getClass().getClassLoader());
		scores = in.readParcelable(getClass().getClassLoader());
	}

	public Personality getPlayer() {
		return player;
	}

	public ScribbleScore getScores() {
		return scores;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(player, flags);
		dest.writeParcelable(scores, flags);
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
