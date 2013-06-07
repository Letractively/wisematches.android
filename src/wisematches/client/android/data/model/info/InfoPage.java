package wisematches.client.android.data.model.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class InfoPage implements Parcelable {
	private final String text;

	public InfoPage(Parcel in) {
		this(in.readString());
	}

	public InfoPage(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(text);
	}

	public static final Parcelable.Creator<InfoPage> CREATOR = new Parcelable.Creator<InfoPage>() {
		public InfoPage createFromParcel(Parcel in) {
			return new InfoPage(in);
		}

		public InfoPage[] newArray(int size) {
			return new InfoPage[size];
		}
	};
}
