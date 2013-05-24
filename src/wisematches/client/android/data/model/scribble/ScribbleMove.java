package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class ScribbleMove implements Parcelable {
	private final int number;
	private final int points;
	private final Date time;
	private final long player;

	protected ScribbleMove(int number, int points, Date time, long player) {
		this.number = number;
		this.points = points;
		this.time = time;
		this.player = player;
	}

	protected ScribbleMove(Parcel in) {
		this.number = in.readInt();
		this.points = in.readInt();
		this.time = new Date(in.readLong());
		this.player = in.readLong();
	}

	public int getNumber() {
		return number;
	}

	public int getPoints() {
		return points;
	}

	public Date getTime() {
		return time;
	}

	public long getPlayer() {
		return player;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(number);
		dest.writeInt(points);
		dest.writeLong(time.getTime());
		dest.writeLong(player);
	}

	public abstract MoveType getMoveType();

	public static final class Make extends ScribbleMove {
		private final ScribbleWord word;

		public Make(int number, int points, Date time, long player, ScribbleWord word) {
			super(number, points, time, player);
			this.word = word;
		}

		protected Make(Parcel in) {
			super(in);
			word = in.readParcelable(getClass().getClassLoader());
		}

		@Override
		public MoveType getMoveType() {
			return MoveType.MAKE;
		}

		public ScribbleWord getWord() {
			return word;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeParcelable(word, flags);
		}

		public static final Parcelable.Creator<Make> CREATOR = new Parcelable.Creator<Make>() {
			public Make createFromParcel(Parcel in) {
				return new Make(in);
			}

			public Make[] newArray(int size) {
				return new Make[size];
			}
		};
	}

	public static final class Pass extends ScribbleMove {
		public Pass(int number, int points, Date time, long player) {
			super(number, points, time, player);
		}

		public Pass(Parcel in) {
			super(in);
		}

		@Override
		public MoveType getMoveType() {
			return MoveType.PASS;
		}

		public static final Parcelable.Creator<Pass> CREATOR = new Parcelable.Creator<Pass>() {
			public Pass createFromParcel(Parcel in) {
				return new Pass(in);
			}

			public Pass[] newArray(int size) {
				return new Pass[size];
			}
		};
	}

	public static final class Exchange extends ScribbleMove {
		public Exchange(int number, int points, Date time, long player) {
			super(number, points, time, player);
		}

		public Exchange(Parcel in) {
			super(in);
		}

		@Override
		public MoveType getMoveType() {
			return MoveType.EXCHANGE;
		}

		public static final Parcelable.Creator<Exchange> CREATOR = new Parcelable.Creator<Exchange>() {
			public Exchange createFromParcel(Parcel in) {
				return new Exchange(in);
			}

			public Exchange[] newArray(int size) {
				return new Exchange[size];
			}
		};
	}
}
