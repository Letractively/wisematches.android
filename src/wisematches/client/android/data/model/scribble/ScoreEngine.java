package wisematches.client.android.data.model.scribble;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScoreEngine implements Parcelable {
	private final int allHandBonus;
	private final ScoreBonus[] scoreBonuses;

	private final ScoreBonus[][] bonusesMatrix = new ScoreBonus[8][8];

	private static final int LETTERS_IN_HAND = 7;

	public ScoreEngine(ScoreBonus[] scoreBonuses, int allHandBonus) {
		this.allHandBonus = allHandBonus;
		this.scoreBonuses = scoreBonuses;

		for (ScoreBonus scoreBonus : scoreBonuses) {
			bonusesMatrix[scoreBonus.getRow()][scoreBonus.getColumn()] = scoreBonus;
		}
	}

	public ScoreEngine(Parcel in) {
		this.allHandBonus = in.readInt();
		this.scoreBonuses = (ScoreBonus[]) in.readParcelableArray(getClass().getClassLoader());

		for (ScoreBonus scoreBonus : scoreBonuses) {
			bonusesMatrix[scoreBonus.getRow()][scoreBonus.getColumn()] = scoreBonus;
		}
	}

	public int getAllHandBonus() {
		return allHandBonus;
	}

	public ScoreBonus[] getScoreBonuses() {
		return scoreBonuses;
	}

	public ScoreBonus getScoreBonus(int row, int col) {
		return bonusesMatrix[row < 8 ? row : 14 - row][col < 8 ? col : 14 - col];
	}

	public ScoreCalculation calculateWordScore(ScribbleBoard board, ScribbleWord word) {
		ScoreBonus.Type[] bonuses = new ScoreBonus.Type[word.length()];

		StringBuilder formula = new StringBuilder();
		StringBuilder mults = new StringBuilder();

		int index = 0;
		int handTilesCount = 0;
		short points = 0;
		int mult = 1;


		for (ScribbleWord.IteratorItem item : word) {
			final int row = item.getRow();
			final int col = item.getColumn();
			final ScribbleTile tile = item.getTile();

			final ScoreBonus scoreBonus = getScoreBonus(row, col);
			final ScoreBonus.Type bonus = scoreBonus != null ? scoreBonus.getType() : null;

			points += tile.getCost();
			if (formula.length() != 0) {
				formula.append("+");
			}
			formula.append(tile.getCost());

			if (!board.isBoardTile(item.getTile().getNumber())) {
				handTilesCount++;

				if (bonus != null) {
					bonuses[index] = bonus;

					switch (bonus) {
						case L2:
							formula.append("*2");
							points += tile.getCost();
							break;
						case L3:
							formula.append("*3");
							points += tile.getCost() * 2;
							break;
						case W2:
							mults.append("*2");
							mult *= 2;
							break;
						case W3:
							mults.append("*3");
							mult *= 3;
							break;
					}
				}
			}
			index++;
		}

		final String multsString = mults.toString(); // In GWT sometimes exception is thrown...
		if (multsString.length() != 0) {
			formula.insert(0, '(');
			formula.append(')');
			formula.append(multsString);
		}
		points *= mult;

		if (allHandBonus != 0 && handTilesCount == LETTERS_IN_HAND) {
			points += allHandBonus;
			if (formula.charAt(0) != '(') {
				formula.insert(0, '(');
				formula.append(')');
			}
			formula.append("+");
			formula.append(allHandBonus);
		}

		return new ScoreCalculation(points, handTilesCount == LETTERS_IN_HAND, bonuses, formula.toString());
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(allHandBonus);
		dest.writeParcelableArray(scoreBonuses, flags);
	}

	public static final Parcelable.Creator<ScoreEngine> CREATOR = new Parcelable.Creator<ScoreEngine>() {
		public ScoreEngine createFromParcel(Parcel in) {
			return new ScoreEngine(in);
		}

		public ScoreEngine[] newArray(int size) {
			return new ScoreEngine[size];
		}
	};
}
