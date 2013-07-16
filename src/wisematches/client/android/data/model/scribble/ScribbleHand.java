package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;
import wisematches.client.android.data.model.person.Personality;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleHand implements Parcelable {
	private final Personality personality;
	private final ScribbleScore scores;

	public ScribbleHand(Personality personality, ScribbleScore scores) {
		this.personality = personality;
		this.scores = scores;
	}

	public ScribbleHand(Parcel in) {
		personality = in.readParcelable(getClass().getClassLoader());
		scores = in.readParcelable(getClass().getClassLoader());
	}

	public ScribbleScore getScores() {
		return scores;
	}

	public Personality getPersonality() {
		return personality;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(personality, flags);
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
