package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;
import wisematches.client.android.data.model.Time;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleStatus implements Parcelable {
	private boolean active;
	private String resolution;

	private Time spentTime;
	private Time startedTime;
	private Time finishedTime;
	private Time remainedTime;

	private long lastChange;
	private long playerTurn;

	public ScribbleStatus(Time startedTime, Time finishedTime, Time remainedTime, Time spentTime, boolean active, String resolution, long playerTurn, long lastChange) {
		this.active = active;
		this.resolution = resolution;
		this.spentTime = spentTime;
		this.startedTime = startedTime;
		this.finishedTime = finishedTime;
		this.remainedTime = remainedTime;
		this.lastChange = lastChange;
		this.playerTurn = playerTurn;
	}

	public ScribbleStatus(Parcel in) {
		ClassLoader classLoader = getClass().getClassLoader();

		active = in.readByte() == 1;
		resolution = in.readString();
		playerTurn = in.readLong();
		spentTime = in.readParcelable(classLoader);
		startedTime = in.readParcelable(classLoader);
		finishedTime = in.readParcelable(classLoader);
		remainedTime = in.readParcelable(classLoader);
		lastChange = in.readLong();
	}

	public boolean isActive() {
		return active;
	}

	public String getResolution() {
		return resolution;
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

	public long getPlayerTurn() {
		return playerTurn;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	void validate(ScribbleStatus status) {
		this.active = status.active;
		this.spentTime = status.spentTime;
		this.lastChange = status.lastChange;
		this.playerTurn = status.playerTurn;
		this.resolution = status.resolution;
		this.finishedTime = status.finishedTime;
		this.remainedTime = status.remainedTime;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte((byte) (active ? 1 : 0));
		dest.writeString(resolution);
		dest.writeLong(playerTurn);
		dest.writeParcelable(spentTime, 0);
		dest.writeParcelable(startedTime, 0);
		dest.writeParcelable(finishedTime, 0);
		dest.writeParcelable(remainedTime, 0);
		dest.writeLong(lastChange);
	}

	public static final Parcelable.Creator<ScribbleStatus> CREATOR = new Parcelable.Creator<ScribbleStatus>() {
		public ScribbleStatus createFromParcel(Parcel in) {
			return new ScribbleStatus(in);
		}

		public ScribbleStatus[] newArray(int size) {
			return new ScribbleStatus[size];
		}
	};
}
