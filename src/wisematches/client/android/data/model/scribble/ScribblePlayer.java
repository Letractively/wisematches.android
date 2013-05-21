package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribblePlayer implements Parcelable {
	public ScribblePlayer() {
	}

	public ScribblePlayer(Parcel in) {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	public static final Parcelable.Creator<ScribblePlayer> CREATOR = new Parcelable.Creator<ScribblePlayer>() {
		public ScribblePlayer createFromParcel(Parcel in) {
			return new ScribblePlayer(in);
		}

		public ScribblePlayer[] newArray(int size) {
			return new ScribblePlayer[size];
		}
	};
}
