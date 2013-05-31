package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;
import wisematches.client.android.data.model.Time;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleDescriptor implements Parcelable {
	private final long id;
	private final ScribbleHand[] players;
	private final ScribbleSettings settings;

	private boolean active;
	private String resolution;

	private int playerTurnIndex;

	private Time spentTime;
	private Time startedTime;
	private Time finishedTime;
	private Time remainedTime;

	private long lastChange;

	public ScribbleDescriptor(long id, ScribbleSettings settings, ScribbleHand[] players, boolean active, String resolution, int playerTurnIndex, Time spentTime, Time startedTime, Time finishedTime, Time remainedTime, long lastChange) {
		this.id = id;
		this.players = players;
		this.settings = settings;
		this.active = active;
		this.resolution = resolution;
		this.playerTurnIndex = playerTurnIndex;
		this.spentTime = spentTime;
		this.startedTime = startedTime;
		this.finishedTime = finishedTime;
		this.remainedTime = remainedTime;
		this.lastChange = lastChange;
	}

	public ScribbleDescriptor(Parcel in) {
		ClassLoader classLoader = getClass().getClassLoader();

		id = in.readLong();
		players = (ScribbleHand[]) in.readParcelableArray(classLoader);
		settings = in.readParcelable(classLoader);
		active = in.readByte() == 1;
		resolution = in.readString();
		playerTurnIndex = in.readInt();
		spentTime = in.readParcelable(classLoader);
		startedTime = in.readParcelable(classLoader);
		finishedTime = in.readParcelable(classLoader);
		remainedTime = in.readParcelable(classLoader);
		lastChange = in.readLong();
	}

	public long getId() {
		return id;
	}

	public ScribbleHand[] getPlayers() {
		return players;
	}

	public ScribbleHand getPlayer(long player) {
		for (ScribbleHand scribbleHand : players) {
			if (scribbleHand.getPlayer().getId() == player) {
				return scribbleHand;
			}
		}
		return null;
	}

	public ScribbleSettings getSettings() {
		return settings;
	}

	public boolean isActive() {
		return active;
	}

	public String getResolution() {
		return resolution;
	}

	public int getPlayerTurnIndex() {
		return playerTurnIndex;
	}

	public Time getSpentTime() {
		return spentTime;
	}

	public Time getStartedTime() {
		return startedTime;
	}

	public Time getFinishedTime() {
		return finishedTime;
	}

	public Time getRemainedTime() {
		return remainedTime;
	}

	public long getLastChange() {
		return lastChange;
	}

	public static Creator<ScribbleDescriptor> getCreator() {
		return CREATOR;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeParcelableArray(players, 0);
		dest.writeParcelable(settings, 0);
		dest.writeByte((byte) (active ? 1 : 0));
		dest.writeString(resolution);
		dest.writeInt(playerTurnIndex);
		dest.writeParcelable(spentTime, 0);
		dest.writeParcelable(startedTime, 0);
		dest.writeParcelable(finishedTime, 0);
		dest.writeParcelable(remainedTime, 0);
		dest.writeLong(lastChange);
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
