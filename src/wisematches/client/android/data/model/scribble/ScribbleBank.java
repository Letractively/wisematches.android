package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleBank implements Parcelable {
	private final int lettersCount;
	private final ScribbleLetter[] distributions;

	public ScribbleBank(ScribbleLetter[] distributions) {
		this.distributions = distributions;

		int cnt = 0;
		for (ScribbleLetter d : distributions) {
			cnt += d.getCount();
		}
		this.lettersCount = cnt;
	}

	public ScribbleBank(Parcel in) {
		this.lettersCount = in.readInt();
		this.distributions = (ScribbleLetter[]) in.readParcelableArray(getClass().getClassLoader());
	}

	public int getLettersCount() {
		return lettersCount;
	}

	public ScribbleLetter[] getDistributions() {
		return distributions;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(lettersCount);
		dest.writeParcelableArray(distributions, flags);
	}

	public static final Parcelable.Creator<ScribbleBank> CREATOR = new Parcelable.Creator<ScribbleBank>() {
		public ScribbleBank createFromParcel(Parcel in) {
			return new ScribbleBank(in);
		}

		public ScribbleBank[] newArray(int size) {
			return new ScribbleBank[size];
		}
	};
}
