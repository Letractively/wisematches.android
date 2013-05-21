package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;
import wisematches.client.android.data.model.Language;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleSettings implements Parcelable {
	private final String title;
	private final int daysPerMove;
	private final Language language;
	private final boolean scratch;

	public ScribbleSettings(String title, int daysPerMove, Language language, boolean scratch) {
		this.title = title;
		this.daysPerMove = daysPerMove;
		this.language = language;
		this.scratch = scratch;
	}

	public ScribbleSettings(Parcel in) {
		this.title = in.readString();
		this.daysPerMove = in.readInt();
		this.language = in.readParcelable(getClass().getClassLoader());
		this.scratch = in.readByte() == 1;
	}

	public String getTitle() {
		return title;
	}

	public int getDaysPerMove() {
		return daysPerMove;
	}

	public Language getLanguage() {
		return language;
	}

	public boolean isScratch() {
		return scratch;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeInt(daysPerMove);
		dest.writeParcelable(language, 0);
		dest.writeByte((byte) (scratch ? 1 : 0));
	}

	public static final Parcelable.Creator<ScribbleSettings> CREATOR = new Parcelable.Creator<ScribbleSettings>() {
		public ScribbleSettings createFromParcel(Parcel in) {
			return new ScribbleSettings(in);
		}

		public ScribbleSettings[] newArray(int size) {
			return new ScribbleSettings[size];
		}
	};
}
