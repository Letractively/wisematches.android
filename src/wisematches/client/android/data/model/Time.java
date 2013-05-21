package wisematches.client.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class Time implements Parcelable {
	private final long millis;
	private final String text;

	public Time(long millis, String text) {
		this.millis = millis;
		this.text = text;
	}

	public Time(Parcel in) {
		this.millis = in.readLong();
		this.text = in.readString();
	}

	public long getMillis() {
		return millis;
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
		dest.writeLong(millis);
		dest.writeString(text);
	}

	public static final Parcelable.Creator<Time> CREATOR = new Parcelable.Creator<Time>() {
		public Time createFromParcel(Parcel in) {
			return new Time(in);
		}

		public Time[] newArray(int size) {
			return new Time[size];
		}
	};
}
