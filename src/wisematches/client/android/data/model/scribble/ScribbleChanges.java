package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleChanges implements Parcelable {
	private final ScribbleStatus status;
	private final ScribbleScore[] scores;
	private final List<ScribbleMove> moves;
	private final ScribbleTile[] handTiles;

	public ScribbleChanges(ScribbleStatus status, ScribbleScore[] scores, List<ScribbleMove> moves, ScribbleTile[] handTiles) {
		this.status = status;
		this.scores = scores;
		this.moves = moves;
		this.handTiles = handTiles;
	}

	@SuppressWarnings("unchecked")
	public ScribbleChanges(Parcel in) {
		final ClassLoader classLoader = getClass().getClassLoader();
		this.status = in.readParcelable(classLoader);
		this.moves = in.readArrayList(classLoader);

		this.scores = new ScribbleScore[in.readInt()];
		in.readTypedArray(this.scores, ScribbleScore.CREATOR);

		this.handTiles = new ScribbleTile[in.readInt()];
		in.readTypedArray(this.handTiles, ScribbleTile.CREATOR);
	}

	public ScribbleStatus getStatus() {
		return status;
	}

	public ScribbleScore[] getScores() {
		return scores;
	}

	public ScribbleTile[] getHandTiles() {
		return handTiles;
	}

	public List<ScribbleMove> getMoves() {
		return moves;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(status, flags);
		dest.writeList(moves);
		dest.writeInt(scores.length);
		dest.writeTypedArray(scores, flags);
		dest.writeInt(handTiles.length);
		dest.writeTypedArray(handTiles, flags);
	}

	public static final Parcelable.Creator<ScribbleChanges> CREATOR = new Parcelable.Creator<ScribbleChanges>() {
		public ScribbleChanges createFromParcel(Parcel in) {
			return new ScribbleChanges(in);
		}

		public ScribbleChanges[] newArray(int size) {
			return new ScribbleChanges[size];
		}
	};
}
