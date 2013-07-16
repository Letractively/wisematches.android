package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleSnapshot implements Parcelable {
	private final ScoreEngine scoreEngine;
	private final ScribbleBank scribbleBank;
	private final List<ScribbleMove> moves;
	private final ScribbleDescriptor descriptor;
	private final ScribbleTile[] handTiles = new ScribbleTile[7];

	public ScribbleSnapshot(ScribbleDescriptor descriptor, ScoreEngine scoreEngine, ScribbleBank scribbleBank, List<ScribbleMove> moves, ScribbleTile[] handTiles) {
		this.descriptor = descriptor;
		this.scoreEngine = scoreEngine;
		this.scribbleBank = scribbleBank;
		this.moves = moves;
		System.arraycopy(handTiles, 0, this.handTiles, 0, handTiles.length);
	}

	@SuppressWarnings("unchecked")
	public ScribbleSnapshot(Parcel in) {
		ClassLoader classLoader = getClass().getClassLoader();
		this.descriptor = in.readParcelable(classLoader);
		this.scoreEngine = in.readParcelable(classLoader);
		this.scribbleBank = in.readParcelable(classLoader);
		this.moves = in.readArrayList(classLoader);
		in.readTypedArray(this.handTiles, ScribbleTile.CREATOR);
	}

	public ScribbleDescriptor getDescriptor() {
		return descriptor;
	}

	public ScoreEngine getScoreEngine() {
		return scoreEngine;
	}

	public List<ScribbleMove> getMoves() {
		return moves;
	}

	public ScribbleTile[] getHandTiles() {
		return handTiles;
	}

	public ScribbleBank getScribbleBank() {
		return scribbleBank;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(descriptor, flags);
		dest.writeParcelable(scoreEngine, flags);
		dest.writeParcelable(scribbleBank, flags);
		dest.writeList(moves);
		dest.writeParcelableArray(handTiles, flags);
	}

	public static final Parcelable.Creator<ScribbleSnapshot> CREATOR = new Parcelable.Creator<ScribbleSnapshot>() {
		public ScribbleSnapshot createFromParcel(Parcel in) {
			return new ScribbleSnapshot(in);
		}

		public ScribbleSnapshot[] newArray(int size) {
			return new ScribbleSnapshot[size];
		}
	};
}
