package wisematches.client.android.graphics;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class Dimension implements Parcelable {
	public int width;
	public int height;

	public Dimension() {
	}

	public Dimension(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Dimension(Dimension src) {
		this.width = src.width;
		this.height = src.height;
	}

	/**
	 * Set the point's width and height coordinates
	 */
	public void set(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Negate the point's coordinates
	 */
	public final void negate() {
		width = -width;
		height = -height;
	}

	/**
	 * Offset the point's coordinates by dw, dh
	 */
	public final void offset(int dw, int dh) {
		width += dw;
		height += dh;
	}

	/**
	 * Returns true if the point's coordinates equal (width,height)
	 */
	public final boolean equals(int width, int height) {
		return this.width == width && this.height == height;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Dimension) {
			Dimension p = (Dimension) o;
			return this.width == p.width && this.height == p.height;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return width * 32713 + height;
	}

	@Override
	public String toString() {
		return "Point(" + width + ", " + height + ")";
	}

	/**
	 * Parcelable interface methods
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * Write this point to the specified parcel. To restore a point from
	 * a parcel, use readFromParcel()
	 *
	 * @param out The parcel to write the point's coordinates into
	 */
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(width);
		out.writeInt(height);
	}

	public static final Parcelable.Creator<Dimension> CREATOR = new Parcelable.Creator<Dimension>() {
		/**
		 * Return a new point from the data in the specified parcel.
		 */
		public Dimension createFromParcel(Parcel in) {
			Dimension r = new Dimension();
			r.readFromParcel(in);
			return r;
		}

		/**
		 * Return an array of rectangles of the specified size.
		 */
		public Dimension[] newArray(int size) {
			return new Dimension[size];
		}
	};

	/**
	 * Set the point's coordinates from the data stored in the specified
	 * parcel. To write a point to a parcel, call writeToParcel().
	 *
	 * @param in The parcel to read the point's coordinates from
	 */
	public void readFromParcel(Parcel in) {
		width = in.readInt();
		height = in.readInt();
	}
}
