package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleDescriptor implements Parcelable {
	private final long id;
	private final ScribbleHand[] players;
	private final ScribbleStatus status;
	private final ScribbleSettings settings;

	public ScribbleDescriptor(long id, ScribbleSettings settings, ScribbleHand[] players, ScribbleStatus status) {
		this.id = id;
		this.status = status;
		this.players = players;
		this.settings = settings;
	}

	public ScribbleDescriptor(Parcel in) {
		ClassLoader classLoader = getClass().getClassLoader();

		id = in.readLong();
		players = (ScribbleHand[]) in.readParcelableArray(classLoader);
		status = in.readParcelable(classLoader);
		settings = in.readParcelable(classLoader);
	}

	public long getId() {
		return id;
	}

	public ScribbleHand[] getPlayers() {
		return players;
	}

	public ScribbleHand getPlayer(long player) {
		for (ScribbleHand scribbleHand : players) {
			if (scribbleHand.getPersonality().getId() == player) {
				return scribbleHand;
			}
		}
		return null;
	}

	public ScribbleStatus getStatus() {
		return status;
	}

	public ScribbleSettings getSettings() {
		return settings;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeParcelableArray(players, flags);
		dest.writeParcelable(status, flags);
		dest.writeParcelable(settings, flags);
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
