package wisematches.client.android.app.playground.scribble.board.surface;

import android.content.res.Resources;
import android.graphics.*;
import wisematches.client.android.R;
import wisematches.client.android.data.model.scribble.ScoreBonus;
import wisematches.client.android.data.model.scribble.ScoreEngine;
import wisematches.client.android.graphics.Dimension;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardSurface {
	private int scale;
	private ScoreEngine scoreEngine;

	private Bitmap boardBackground;

	private final String boardCaption;
	private final String[] bonusCaptions;

	private final Rect handRegion = new Rect();
	private final Rect boardRegion = new Rect();
	private final Dimension dimension = new Dimension();

	private static final int MAGIC_COEF = 8;
	private static final int BORDER_SIZE = 3;

	public BoardSurface(Resources resources) {
		this.boardCaption = resources.getString(R.string.board_surface_captions);

		final ScoreBonus.Type[] values = ScoreBonus.Type.values();
		this.bonusCaptions = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			final ScoreBonus.Type value = values[i];
			bonusCaptions[i] = resources.getString(resources.getIdentifier("board_surface_bonus_" + value.name(), "string", "wisematches.client.android"));
		}
	}

	public void initScoreEngine(ScoreEngine scoreEngine) {
		boardBackground = null;

		this.scoreEngine = scoreEngine;
	}

	public Dimension onMeasure(int parentWidth, int parentHeight) {
		boardBackground = null;

		if (parentHeight != 0) {
			scale = (parentHeight - 8) / 17;
			if (scale % 2 != 0) {
				scale -= 1;
			}
			final int width = (BORDER_SIZE + scale / 2) * 2 + scale * 15;
			final int height = BORDER_SIZE + scale / 2 + scale * 15 + scale + BORDER_SIZE;
			dimension.set(width, height);
		}
		return dimension;
	}

	public void drawBackground(Canvas canvas) {
		if (boardBackground == null) {
			createBoardView();
		}

		canvas.drawBitmap(boardBackground, 0, 0, null);
	}


	@Deprecated
	public int getScale() {
		return scale;
	}

	@Deprecated
	public Rect getHandRegion() {
		return handRegion;
	}

	@Deprecated
	public Rect getBoardRegion() {
		return boardRegion;
	}

	private void createBoardView() {
		final int border = scale / 2;

		boardBackground = Bitmap.createBitmap(dimension.width, dimension.height, Bitmap.Config.ARGB_8888);

		final Canvas canvas = new Canvas(boardBackground);

		final Paint paint = new Paint();
		paint.setStrokeWidth(1f);

		final Rect rect = new Rect();

		rect.set(0, 0, dimension.width, dimension.width);
		paint.setARGB(0xff, 0xca, 0xdb, 0xe1);
		canvas.drawRect(rect, paint);

		rect.inset(1, 1);
		paint.setARGB(0xff, 0xda, 0xec, 0xf2);
		canvas.drawRect(rect, paint);

		rect.inset(border, border);
		paint.setARGB(0xff, 0x00, 0x00, 0x00);
		canvas.drawRect(rect, paint);

		rect.inset(1, 1);
		paint.setARGB(0xff, 0xff, 0xff, 0xff);
		canvas.drawRect(rect, paint);

		rect.inset(1, 1);
		final Paint p = new Paint();
		p.setShader(new LinearGradient(rect.left, rect.top, rect.right, rect.bottom, 0xFF1947d2, 0xFF75b0f1, Shader.TileMode.CLAMP));
		canvas.drawRect(rect, p);

		paint.setTextSize(border);
		paint.setAntiAlias(true);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setARGB(0xff, 0x00, 0x00, 0x00);

		final char[] letters = boardCaption.toCharArray();
		for (int i = 0; i < 15; i++) {
			canvas.drawText(String.valueOf(letters[i]), rect.left - border + scale / MAGIC_COEF, rect.top + border + (border - BORDER_SIZE) / 2 + scale * i, paint);
			canvas.drawText(String.valueOf(i + 1), rect.left + border + scale * i, border - 1, paint);
			canvas.drawText(String.valueOf(letters[i]), rect.right + border - scale / MAGIC_COEF - 1, rect.top + border + (border - BORDER_SIZE) / 2 + scale * i, paint);
			canvas.drawText(String.valueOf(i + 1), rect.left + border + scale * i, rect.bottom + border, paint);
		}

		paint.setAntiAlias(false);
		paint.setTextSize(border);
		paint.setTextAlign(Paint.Align.CENTER);


		boardRegion.set(rect);

		paint.setColor(Color.WHITE);
		for (int i = 0; i < 15; i++) {
			canvas.drawLine(rect.left + border + scale * i, rect.top, rect.left + border + scale * i, rect.bottom, paint);
			canvas.drawLine(rect.left, rect.top + border + scale * i, rect.right, rect.top + border + scale * i, paint);
		}

		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				final ScoreBonus bonus = scoreEngine.getScoreBonus(i, j);

				int px = rect.left + border;
				int py = rect.right - border;
				if (bonus != null) {
					paint.setAntiAlias(true);
					int r = bonus.getRow();
					int c = bonus.getColumn();

					final float radius = border - 2;
					paint.setColor(bonus.getType().getColor());
					canvas.drawCircle(px + scale * r, px + scale * c, radius, paint);
					canvas.drawCircle(py - scale * r, py - scale * c, radius, paint);
					canvas.drawCircle(px + scale * r, py - scale * c, radius, paint);
					canvas.drawCircle(py - scale * r, px + scale * c, radius, paint);

					paint.setColor(Color.BLACK);

					final String caption = bonusCaptions[bonus.getType().ordinal()];
					canvas.drawText(caption, px + scale * r, px + 4 + scale * c, paint);
					canvas.drawText(caption, py - scale * r, py + 5 - scale * c, paint);
					canvas.drawText(caption, px + scale * r, py + 5 - scale * c, paint);
					canvas.drawText(caption, py - scale * r, px + 4 + scale * c, paint);
					paint.setAntiAlias(false);
				} else {
					paint.setColor(Color.WHITE);
					canvas.drawLine((px + scale * i) - 2, (px + scale * j) - 1, (px + scale * i) + BORDER_SIZE, (px + scale * j) - 1, paint);
					canvas.drawLine((px + scale * i) - 1, (px + scale * j) - 2, (px + scale * i) + 2, (px + scale * j) - 2, paint);
					canvas.drawLine((px + scale * i) - 2, (px + scale * j) + 1, (px + scale * i) + BORDER_SIZE, (px + scale * j) + 1, paint);
					canvas.drawLine((px + scale * i) - 1, (px + scale * j) + 2, (px + scale * i) + 2, (px + scale * j) + 2, paint);
				}
			}
		}
		paint.setAntiAlias(false);

		handRegion.set(rect.left + scale * 4, rect.bottom + 1, rect.right - scale * 4, rect.bottom + scale + 1);

		final Path path = new Path();
		path.moveTo(handRegion.left - scale / 2, handRegion.top);
		path.lineTo(handRegion.left, handRegion.bottom);
		path.lineTo(handRegion.right, handRegion.bottom);
		path.lineTo(handRegion.right + scale / 2, handRegion.top);
		path.close();

		paint.setARGB(0xFF, 0x55, 0x8b, 0xe7);
		canvas.drawPath(path, paint);

		paint.setColor(Color.BLACK);
		canvas.drawLine(handRegion.left - scale / 2, handRegion.top, handRegion.left, handRegion.bottom, paint);
		canvas.drawLine(handRegion.left, handRegion.bottom, handRegion.right, handRegion.bottom, paint);
		canvas.drawLine(handRegion.right, handRegion.bottom, handRegion.right + scale / 2, handRegion.top, paint);

		paint.setColor(Color.WHITE);
		canvas.drawLine(handRegion.left - scale / 2 + 1, handRegion.top, handRegion.left + 1, handRegion.bottom - 1, paint);
		canvas.drawLine(handRegion.left + 1, handRegion.bottom - 1, handRegion.right - 1, handRegion.bottom - 1, paint);
		canvas.drawLine(handRegion.right - 1, handRegion.bottom - 1, handRegion.right + scale / 2 - 1, handRegion.top, paint);
	}
}
