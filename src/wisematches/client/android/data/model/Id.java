package wisematches.client.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class Id implements Parcelable {
	private final long id;

	public Id(long id) {
		this.id = id;
	}

	public Id(Parcel in) {
		this.id = in.readLong();
	}

	public long getId() {
		return id;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
	}

	public static final Parcelable.Creator<Id> CREATOR = new Parcelable.Creator<Id>() {
		public Id createFromParcel(Parcel in) {
			return new Id(in);
		}

		public Id[] newArray(int size) {
			return new Id[size];
		}
	};
}
