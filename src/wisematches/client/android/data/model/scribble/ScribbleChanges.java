package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleChanges implements Parcelable {
	public ScribbleChanges() {
	}

	public ScribbleChanges(Parcel in) {
		this();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	public static final Parcelable.Creator<ScribbleChanges> CREATOR = new Parcelable.Creator<ScribbleChanges>() {
		public ScribbleChanges createFromParcel(Parcel in) {
			return new ScribbleChanges(in);
		}

		public ScribbleChanges[] newArray(int size) {
			return new ScribbleChanges[size];
		}
	};
}
