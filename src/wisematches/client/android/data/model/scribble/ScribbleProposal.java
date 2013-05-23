package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;
import wisematches.client.android.data.model.Time;
import wisematches.client.android.data.model.person.Personality;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleProposal implements Parcelable {
	private final long id;
	private final ScribbleSettings settings;
	private final Time creationDate;
	private final long initiator;
	private final Personality[] players;
	private final long[] joinedPlayers;
	private final boolean ready;
	private final String proposalType;
	private final CriterionViolation[] violations;

	public ScribbleProposal(long id, ScribbleSettings settings, Time creationDate, long initiator, Personality[] players, long[] joinedPlayers, boolean ready, String proposalType, CriterionViolation[] violations) {
		this.id = id;
		this.settings = settings;
		this.creationDate = creationDate;
		this.initiator = initiator;
		this.players = players;
		this.joinedPlayers = joinedPlayers;
		this.ready = ready;
		this.proposalType = proposalType;
		this.violations = violations;
	}

	public ScribbleProposal(Parcel in) {
		final ClassLoader classLoader = getClass().getClassLoader();

		this.id = in.readLong();
		this.settings = in.readParcelable(classLoader);
		this.creationDate = in.readParcelable(classLoader);
		this.initiator = in.readLong();
		this.players = (Personality[]) in.readParcelableArray(classLoader);
		this.joinedPlayers = in.createLongArray();
		this.ready = in.readByte() == 1;
		this.proposalType = in.readString();
		this.violations = (CriterionViolation[]) in.readParcelableArray(classLoader);
	}

	public long getId() {
		return id;
	}

	public ScribbleSettings getSettings() {
		return settings;
	}

	public Time getCreationDate() {
		return creationDate;
	}

	public long getInitiator() {
		return initiator;
	}

	public Personality[] getPlayers() {
		return players;
	}

	public long[] getJoinedPlayers() {
		return joinedPlayers;
	}

	public boolean isReady() {
		return ready;
	}

	public String getProposalType() {
		return proposalType;
	}

	public CriterionViolation[] getViolations() {
		return violations;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeParcelable(settings, flags);
		dest.writeParcelable(creationDate, flags);
		dest.writeLong(initiator);
		dest.writeParcelableArray(players, flags);
		dest.writeLongArray(joinedPlayers);
		dest.writeByte((byte) (ready ? 1 : 0));
		dest.writeString(proposalType);
		dest.writeParcelableArray(violations, flags);
	}

	public static final Parcelable.Creator<ScribbleProposal> CREATOR = new Parcelable.Creator<ScribbleProposal>() {
		public ScribbleProposal createFromParcel(Parcel in) {
			return new ScribbleProposal(in);
		}

		public ScribbleProposal[] newArray(int size) {
			return new ScribbleProposal[size];
		}
	};
}
