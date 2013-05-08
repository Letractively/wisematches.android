package wisematches.client.android.data.model.person;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class Personality implements Parcelable {
	private final long id;

	public Personality(long id, String nickname, String language, TimeZone timeZone, String type, String membership, boolean online) {
		this.id = id;
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
	}
}
