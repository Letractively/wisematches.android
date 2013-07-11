package wisematches.client.android.app.playground.view.theme;

import android.content.res.Resources;
import android.graphics.*;
import wisematches.client.android.R;
import wisematches.client.android.data.model.scribble.ScoreBonus;
import wisematches.client.android.data.model.scribble.ScoreEngine;
import wisematches.client.android.data.model.scribble.ScribbleTile;
import wisematches.client.android.graphics.Dimension;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardSurface {
	private int scale;

	private final String boardCaption;
	private final String[] bonusCaptions;

	private final Rect handRegion = new Rect();
	private final Rect boardRegion = new Rect();

	private final Dimension dimension = new Dimension();

	private final Bitmap[] tilesSelected = new Bitmap[11];
	private final Bitmap[] tilesUnselected = new Bitmap[11];
	private final Bitmap[] tilesPinnedSelected = new Bitmap[11];
	private final Bitmap[] tilesPinnedUnselected = new Bitmap[11];

	private final Bitmap[] tilesHighlighters = new Bitmap[11];

	private static final int MAGIC_COEF = 8;
	private static final int BORDER_SIZE = 3;
	private static final float MAGIC_TEXT_COEF = 0.6f;

	public BoardSurface(Resources resources) {
		final Bitmap bitmap = android.graphics.BitmapFactory.decodeResource(resources, R.drawable.board_tiles);

		final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 242, 110, true);
		for (int i = 0; i < 11; i++) {
			tilesHighlighters[i] = Bitmap.createBitmap(scaledBitmap, 22 * i, 88, 22, 22);

			tilesSelected[i] = Bitmap.createBitmap(scaledBitmap, 22 * i, 22, 22, 22);
			tilesUnselected[i] = Bitmap.createBitmap(scaledBitmap, 22 * i, 0, 22, 22);
			tilesPinnedSelected[i] = Bitmap.createBitmap(scaledBitmap, 22 * i, 66, 22, 22);
			tilesPinnedUnselected[i] = Bitmap.createBitmap(scaledBitmap, 22 * i, 44, 22, 22);
		}

		final ScoreBonus.Type[] values = ScoreBonus.Type.values();
		bonusCaptions = new String[values.length];
		boardCaption = resources.getString(R.string.board_surface_captions);
		for (int i = 0; i < values.length; i++) {
			final ScoreBonus.Type value = values[i];
			try {
				bonusCaptions[i] = resources.getString(resources.getIdentifier("board_surface_bonus_" + value.name(), "string", "wisematches.client.android"));
			} catch (Exception ignore) {
			}
		}
	}

	public Dimension initialize(int width, int height) {
		scale = (height - 8) / 17;
		if (scale % 2 != 0) {
			scale -= 1;
		}
		final int w = (BORDER_SIZE + scale / 2) * 2 + scale * 15;
		final int h = BORDER_SIZE + scale / 2 + scale * 15 + scale + BORDER_SIZE;
		dimension.set(w, h);
		return dimension;
	}

	public void drawBackground(Canvas canvas, ScoreEngine scoreEngine) {
		final int border = scale / 2;

		final Paint paint = new Paint();
		paint.setStrokeWidth(1f);

		final Rect rect = new Rect(0, 0, dimension.width, dimension.width);
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

	public void drawHighlighter(Canvas canvas, ScribbleTile tile, Placement placement) {
		if (placement.isEmpty()) {
			return;
		}

		final Bitmap bitmap = Bitmap.createScaledBitmap(tilesHighlighters[tile.getCost()], scale, scale, false);

		int x = placement.x;
		int y = placement.y;
		switch (placement.place) {
			case BOARD:
				y = boardRegion.top + scale * y;
				x = boardRegion.left + scale * x;
				break;
			case HAND:
				y = handRegion.top;
				x = handRegion.left + scale * x;
				break;
		}

		canvas.drawBitmap(bitmap, x, y, null);
	}

	public void drawScribbleTile(Canvas canvas, ScribbleTile tile, Placement placement, boolean selected, boolean pinned) {
		Bitmap bitmap;
		if (selected && pinned) {
			bitmap = tilesPinnedSelected[tile.getCost()];
		} else if (selected) {
			bitmap = tilesSelected[tile.getCost()];
		} else if (pinned) {
			bitmap = tilesPinnedUnselected[tile.getCost()];
		} else {
			bitmap = tilesUnselected[tile.getCost()];
		}

		final Paint paint = new Paint();
		paint.setTextSize(scale * MAGIC_TEXT_COEF);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setFakeBoldText(selected);

		bitmap = Bitmap.createScaledBitmap(bitmap, scale, scale, false);

		int x = placement.x;
		int y = placement.y;
		switch (placement.place) {
			case BOARD:
				y = boardRegion.top + scale * y;
				x = boardRegion.left + scale * x;
				break;
			case HAND:
				y = handRegion.top;
				x = handRegion.left + scale * x;
				break;
		}

		canvas.drawBitmap(bitmap, x, y, paint);

		paint.setAntiAlias(true);
		if (tile.isWildcard()) {
			paint.setColor(Color.BLACK);
		} else {
			paint.setColor(Color.WHITE);
		}

		canvas.drawText(tile.getLetter(), x + scale / 2, y + (scale - paint.ascent()) / 2 - 1, paint);
		paint.setAntiAlias(false);
	}

	public Placement getPlacement(int x, int y) {
		if (handRegion.contains(x, y)) {
			return new Placement((x - handRegion.left) / scale, 0, Place.HAND);
		}

		if (boardRegion.contains(x, y)) {
			return new Placement((x - boardRegion.left) / scale, (y - boardRegion.top) / scale, Place.BOARD);
		}
		return null;
	}

	public void fillRelativePosition(Placement placement, Point position) {
		switch (placement.place) {
			case HAND:
				position.set(handRegion.left + placement.x * scale, handRegion.top);
				break;
			case BOARD:
				position.set(boardRegion.left + placement.x * scale, boardRegion.top + placement.y * scale);
				break;
			case RELATIVE:
				position.set(placement.x, placement.y);
				break;
		}
	}

	public static enum Place {
		HAND,
		BOARD,
		RELATIVE
	}

	public static class Placement {
		private int x;
		private int y;
		private Place place;

		public Placement() {
		}

		public Placement(Placement placement) {
			set(placement);
		}

		public Placement(int x, int y, Place place) {
			set(x, y, place);
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public Place getPlace() {
			return place;
		}

		public void set(int x, int y, Place place) {
			this.x = x;
			this.y = y;
			this.place = place;
		}

		public void set(Placement placement) {
			if (placement == null) {
				clear();
			} else {
				this.x = placement.x;
				this.y = placement.y;
				this.place = placement.place;
			}
		}

		public boolean in(Place place) {
			return this.place == place;
		}

		public boolean at(int x, int y) {
			return (this.x == x) && (this.y == y);
		}

		public boolean isEmpty() {
			return x == 0 && y == 0 && place == null;
		}

		public void clear() {
			x = 0;
			y = 0;
			place = null;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Placement)) return false;

			Placement placement = (Placement) o;
			return x == placement.x && y == placement.y && place == placement.place;
		}

		@Override
		public int hashCode() {
			int result = x;
			result = 31 * result + y;
			result = 31 * result + (place != null ? place.hashCode() : 0);
			return result;
		}
	}
}
