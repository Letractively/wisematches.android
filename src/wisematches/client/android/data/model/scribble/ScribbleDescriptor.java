package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;
import wisematches.client.android.data.model.person.Personality;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleDescriptor implements Parcelable {
	private final long boardId;
	private final String title;
	private final String startedDate;
	private final String finishedDate;
	private final String language;
	private final String resolution;
	private final int movesCount;
	private final int playerTurnIndex;
	private final Personality[] players;
	private final ScribbleScore[] scores;

	public ScribbleDescriptor(long boardId, String title, String startedDate, String finishedDate, String language, String resolution, int movesCount, int playerTurnIndex, Personality[] players, ScribbleScore[] scores) {
		this.boardId = boardId;
		this.title = title;
		this.startedDate = startedDate;
		this.finishedDate = finishedDate;
		this.language = language;
		this.resolution = resolution;
		this.movesCount = movesCount;
		this.playerTurnIndex = playerTurnIndex;
		this.players = players;
		this.scores = scores;
	}

	public ScribbleDescriptor(Parcel in) {
		this.boardId = in.readLong();
		this.title = in.readString();
		this.startedDate = in.readString();
		this.finishedDate = in.readString();
		this.language = in.readString();
		this.resolution = in.readString();
		this.movesCount = in.readInt();
		this.playerTurnIndex = in.readInt();

		final ClassLoader classLoader = getClass().getClassLoader();
		this.players = (Personality[]) in.readParcelableArray(classLoader);
		this.scores = (ScribbleScore[]) in.readParcelableArray(classLoader);
	}

	public long getBoardId() {
		return boardId;
	}

	public String getTitle() {
		return title;
	}

	public String getStartedDate() {
		return startedDate;
	}

	public String getFinishedDate() {
		return finishedDate;
	}

	public String getLanguage() {
		return language;
	}

	public String getResolution() {
		return resolution;
	}

	public int getMovesCount() {
		return movesCount;
	}

	public int getPlayersCount() {
		return players.length;
	}

	public int getPlayerTurnIndex() {
		return playerTurnIndex;
	}

	public Personality[] getPlayers() {
		return players;
	}

	public ScribbleScore[] getScores() {
		return scores;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.boardId);
		dest.writeString(this.title);
		dest.writeString(this.startedDate);
		dest.writeString(this.finishedDate);
		dest.writeString(this.language);
		dest.writeString(this.resolution);
		dest.writeInt(this.movesCount);
		dest.writeInt(this.playerTurnIndex);

		dest.writeParcelableArray(this.players, 0);
		dest.writeParcelableArray(this.scores, 0);
	}

	public static final Parcelable.Creator<ScribbleDescriptor> CREATOR = new Parcelable.Creator<ScribbleDescriptor>() {
		public ScribbleDescriptor createFromParcel(Parcel in) {
			return new ScribbleDescriptor(in);
		}

		public ScribbleDescriptor[] newArray(int size) {
			return new ScribbleDescriptor[size];
		}
	};
}
