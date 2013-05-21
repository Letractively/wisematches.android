package wisematches.client.android.data.model.person;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class Personality implements Parcelable {
	private final long id;
	private final String nickname;
	private final String language;
	private final TimeZone timeZone;
	private final String type;
	private final String membership;
	private final boolean online;

	public Personality(long id, String nickname, String language, TimeZone timeZone, String type, String membership, boolean online) {
		this.id = id;
		this.nickname = nickname;
		this.language = language;
		this.timeZone = timeZone;
		this.type = type;
		this.membership = membership;
		this.online = online;
	}

	public Personality(Bundle bundle) {
		id = bundle.getLong("id");
		nickname = bundle.getString("nickname");
		language = bundle.getString("language");

		final String tz = bundle.getString("timeZone");
		if (tz != null) {
			this.timeZone = TimeZone.getTimeZone(tz);
		} else {
			this.timeZone = null;
		}
		type = bundle.getString("type");
		membership = bundle.getString("membership");
		online = bundle.getBoolean("online");
	}

	public Personality(Parcel in) {
		this.id = in.readLong();
		this.nickname = in.readString();
		this.language = in.readString();

		final String tz = in.readString();
		if (tz != null) {
			this.timeZone = TimeZone.getTimeZone(tz);
		} else {
			this.timeZone = null;
		}
		this.type = in.readString();
		this.membership = in.readString();
		this.online = in.readByte() != 0;
	}

	public long getId() {
		return id;
	}

	public String getNickname() {
		return nickname;
	}

	public String getLanguage() {
		return language;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public String getType() {
		return type;
	}

	public String getMembership() {
		return membership;
	}

	public boolean isOnline() {
		return online;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(nickname);
		dest.writeString(language);
		if (timeZone != null) {
			dest.writeString(timeZone.getID());
		} else {
			dest.writeString(null);
		}
		dest.writeString(type);
		dest.writeString(membership);
		dest.writeByte((byte) (online ? 1 : 0));
	}

	public Bundle writeToBundle(Bundle dest) {
		dest.putLong("id", id);
		dest.putString("nickname", nickname);
		dest.putString("language", language);
		if (timeZone != null) {
			dest.putString("timeZone", timeZone.getID());
		} else {
			dest.putString("timeZone", null);
		}
		dest.putString("type", type);
		dest.putString("membership", membership);
		dest.putBoolean("online", online);
		return dest;
	}

	public static final Parcelable.Creator<Personality> CREATOR = new Parcelable.Creator<Personality>() {
		public Personality createFromParcel(Parcel in) {
			return new Personality(in);
		}

		public Personality[] newArray(int size) {
			return new Personality[size];
		}
	};
}
