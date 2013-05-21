package wisematches.client.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import wisematches.client.android.R;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum Language implements Parcelable {
	RU(R.string.language_ru),
	EN(R.string.language_en);

	private final String code;
	private final int resourceId;

	private Language(int resourceId) {
		this.code = name().toLowerCase();
		this.resourceId = resourceId;
	}

	public String getCode() {
		return code;
	}

	public int getResourceId() {
		return resourceId;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(code);
	}

	public static final Parcelable.Creator<Language> CREATOR = new Parcelable.Creator<Language>() {
		public Language createFromParcel(Parcel in) {
			return Language.valueOf(in.readString().toUpperCase());
		}

		public Language[] newArray(int size) {
			return new Language[size];
		}
	};
}
