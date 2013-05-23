package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ActiveGames implements Parcelable {
	private final ScribbleProposal[] proposals;
	private final ScribbleDescriptor[] descriptors;

	public ActiveGames(ScribbleProposal[] proposals, ScribbleDescriptor[] descriptors) {
		this.proposals = proposals;
		this.descriptors = descriptors;
	}

	public ActiveGames(Parcel in) {
		this.proposals = (ScribbleProposal[]) in.readParcelableArray(getClass().getClassLoader());
		this.descriptors = (ScribbleDescriptor[]) in.readParcelableArray(getClass().getClassLoader());
	}

	public ScribbleProposal[] getProposals() {
		return proposals;
	}

	public ScribbleDescriptor[] getDescriptors() {
		return descriptors;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeArray(proposals);
		dest.writeArray(descriptors);
	}

	public static final Parcelable.Creator<ActiveGames> CREATOR = new Parcelable.Creator<ActiveGames>() {
		public ActiveGames createFromParcel(Parcel in) {
			return new ActiveGames(in);
		}

		public ActiveGames[] newArray(int size) {
			return new ActiveGames[size];
		}
	};
}
