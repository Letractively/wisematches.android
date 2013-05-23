package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WaitingGames implements Parcelable {
	private ScribbleProposal[] proposals;
	private CriterionViolation[] globalViolations;

	public WaitingGames(ScribbleProposal[] proposals, CriterionViolation[] globalViolations) {
		this.proposals = proposals;
		this.globalViolations = globalViolations;
	}

	public WaitingGames(Parcel in) {
		this.proposals = (ScribbleProposal[]) in.readArray(getClass().getClassLoader());
		this.globalViolations = (CriterionViolation[]) in.readArray(getClass().getClassLoader());
	}

	public ScribbleProposal[] getProposals() {
		return proposals;
	}

	public CriterionViolation[] getGlobalViolations() {
		return globalViolations;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeArray(proposals);
		dest.writeArray(globalViolations);
	}

	public static final Parcelable.Creator<WaitingGames> CREATOR = new Parcelable.Creator<WaitingGames>() {
		public WaitingGames createFromParcel(Parcel in) {
			return new WaitingGames(in);
		}

		public WaitingGames[] newArray(int size) {
			return new WaitingGames[size];
		}
	};
}
