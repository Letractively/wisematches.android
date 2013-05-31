package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WordEntry implements Parcelable {
	private final String word;
	private final String definition;
	private final String[] attributes;

	public WordEntry(String word, String definition, String[] attributes) {
		this.word = word;
		this.definition = definition;
		this.attributes = attributes;
	}

	public WordEntry(Parcel in) {
		this(in.readString(), in.readString(), in.createStringArray());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(word);
		dest.writeString(definition);
		dest.writeStringArray(attributes);
	}

	public static final Parcelable.Creator<WordEntry> CREATOR = new Parcelable.Creator<WordEntry>() {
		public WordEntry createFromParcel(Parcel in) {
			return new WordEntry(in);
		}

		public WordEntry[] newArray(int size) {
			return new WordEntry[size];
		}
	};
}
