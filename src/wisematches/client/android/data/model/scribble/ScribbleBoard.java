package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleBoard extends ScribbleDescriptor {
	private final ScoreEngine scoreEngine;
	private final ScribbleBank scribbleBank;
	private final List<ScribbleMove> moves;

	private final Set<Integer> placedTiles = new HashSet<>();

	public ScribbleBoard(ScribbleDescriptor descriptor, ScoreEngine scoreEngine, ScribbleBank scribbleBank, List<ScribbleMove> moves) {
		super(descriptor.getId(), descriptor.getSettings(), descriptor.getPlayers(), descriptor.isActive(),
				descriptor.getResolution(), descriptor.getPlayerTurnIndex(),
				descriptor.getSpentTime(), descriptor.getStartedTime(), descriptor.getFinishedTime(),
				descriptor.getRemainedTime(), descriptor.getLastChange());
		this.scoreEngine = scoreEngine;
		this.scribbleBank = scribbleBank;
		this.moves = moves;
	}

	@SuppressWarnings("unchecked")
	public ScribbleBoard(Parcel in) {
		super(in);
		ClassLoader classLoader = getClass().getClassLoader();
		this.scoreEngine = in.readParcelable(classLoader);
		this.scribbleBank = in.readParcelable(classLoader);
		this.moves = in.readArrayList(classLoader);
	}

	@Deprecated
	public int getBoardTilesCount() {
		return 10;
	}

	public ScoreEngine getScoreEngine() {
		return scoreEngine;
	}

	public List<ScribbleMove> getMoves() {
		return moves;
	}

	public ScribbleBank getScribbleBank() {
		return scribbleBank;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);

		dest.writeParcelable(scoreEngine, flags);
		dest.writeParcelable(scribbleBank, flags);
		dest.writeList(moves);
	}

	public static final Parcelable.Creator<ScribbleBoard> CREATOR = new Parcelable.Creator<ScribbleBoard>() {
		public ScribbleBoard createFromParcel(Parcel in) {
			return new ScribbleBoard(in);
		}

		public ScribbleBoard[] newArray(int size) {
			return new ScribbleBoard[size];
		}
	};
}
