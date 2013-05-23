package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class CriterionViolation implements Parcelable {
	private final String code;
	private final String longDescription;
	private final String shortDescription;

	public CriterionViolation(String code, String longDescription, String shortDescription) {
		this.code = code;
		this.longDescription = longDescription;
		this.shortDescription = shortDescription;
	}

	public CriterionViolation(Parcel in) {
		this.code = in.readString();
		this.longDescription = in.readString();
		this.shortDescription = in.readString();
	}

	public String getCode() {
		return code;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(code);
		dest.writeString(longDescription);
		dest.writeString(shortDescription);
	}

	public static final Parcelable.Creator<CriterionViolation> CREATOR = new Parcelable.Creator<CriterionViolation>() {
		public CriterionViolation createFromParcel(Parcel in) {
			return new CriterionViolation(in);
		}

		public CriterionViolation[] newArray(int size) {
			return new CriterionViolation[size];
		}
	};
}
