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
public class BoardSurfaceHorz {
	private int scale;

	private int boardScale;
	private Point boardPosition = new Point(0, 0);

	private final String boardCaption;
	private final String[] bonusCaptions;

	private final Rect handRegion = new Rect();
	private final Rect boardRegion = new Rect();

	private final TileSurface tileSurface;
	private boolean drawCaptions = false;

	private final Dimension dimension = new Dimension();

	private static final int MAGIC_COEF = 8;
	private static final int BORDER_SIZE = 3;

	public BoardSurfaceHorz(Resources resources) {
		tileSurface = new TileSurface(resources);

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
		scale = (height - BORDER_SIZE * 2) / 15;
		if (scale % 2 != 0) {
			scale -= 1;
		}
		final int i = drawCaptions ? scale / 2 : 0;
		final int w = (BORDER_SIZE + i) * 2 + scale * 15 + scale + 4;
		final int h = (BORDER_SIZE + i) * 2 + scale * 15;
		dimension.set(w, h);
		return dimension;
	}

	public void drawBackground(Canvas canvas, ScoreEngine scoreEngine) {
		final int border = scale / 2;

		final int backgroundScale = scale;

		final Paint paint = new Paint();
		paint.setStrokeWidth(1f);

		canvas.save();
		final Rect rect = new Rect(0, 0, dimension.height, dimension.height);
//		canvas.clipRect(rect);

		if (drawCaptions) {
			paint.setARGB(0xff, 0xca, 0xdb, 0xe1);
			canvas.drawRect(rect, paint);

			rect.inset(1, 1);
			paint.setARGB(0xff, 0xda, 0xec, 0xf2);
			canvas.drawRect(rect, paint);

			rect.inset(border, border);
		}
		paint.setARGB(0xff, 0x00, 0x00, 0x00);
		canvas.drawRect(rect, paint);

		rect.inset(1, 1);
		paint.setARGB(0xff, 0xff, 0xff, 0xff);
		canvas.drawRect(rect, paint);

		rect.inset(1, 1);
		final Paint p = new Paint();
		p.setShader(new LinearGradient(rect.left, rect.top, rect.right, rect.bottom, 0xFF1947d2, 0xFF75b0f1, Shader.TileMode.CLAMP));
		canvas.drawRect(rect, p);

		boardRegion.set(rect);

		if (drawCaptions) {
			paint.setTextSize(border);
			paint.setAntiAlias(true);
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);

			final char[] letters = boardCaption.toCharArray();
			for (int i = 0; i < 15; i++) {
				canvas.drawText(String.valueOf(letters[i]), rect.left - border + backgroundScale / MAGIC_COEF, rect.top + border + (border - BORDER_SIZE) / 2 + backgroundScale * i, paint);
				canvas.drawText(String.valueOf(i + 1), rect.left + border + backgroundScale * i, border - 1, paint);
				canvas.drawText(String.valueOf(letters[i]), rect.right + border - backgroundScale / MAGIC_COEF - 1, rect.top + border + (border - BORDER_SIZE) / 2 + backgroundScale * i, paint);
				canvas.drawText(String.valueOf(i + 1), rect.left + border + backgroundScale * i, rect.bottom + border, paint);
			}
		}

		paint.setAntiAlias(false);
		paint.setTextSize(border);
		paint.setColor(Color.WHITE);
		paint.setTextAlign(Paint.Align.CENTER);

		for (int i = 0; i < 15; i++) {
			canvas.drawLine(rect.left + border + backgroundScale * i, rect.top, rect.left + border + backgroundScale * i, rect.bottom, paint);
			canvas.drawLine(rect.left, rect.top + border + backgroundScale * i, rect.right, rect.top + border + backgroundScale * i, paint);
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

					final float radius = border - 3;
					paint.setColor(bonus.getType().getColor());
					canvas.drawCircle(px + backgroundScale * r, px + backgroundScale * c, radius, paint);
					canvas.drawCircle(py - backgroundScale * r, py - backgroundScale * c, radius, paint);
					canvas.drawCircle(px + backgroundScale * r, py - backgroundScale * c, radius, paint);
					canvas.drawCircle(py - backgroundScale * r, px + backgroundScale * c, radius, paint);

					paint.setColor(Color.BLACK);

					final String caption = bonusCaptions[bonus.getType().ordinal()];
					canvas.drawText(caption, px + backgroundScale * r, px + 4 + backgroundScale * c, paint);
					canvas.drawText(caption, py - backgroundScale * r, py + 5 - backgroundScale * c, paint);
					canvas.drawText(caption, px + backgroundScale * r, py + 5 - backgroundScale * c, paint);
					canvas.drawText(caption, py - backgroundScale * r, px + 4 + backgroundScale * c, paint);
					paint.setAntiAlias(false);
				} else {
					paint.setColor(Color.WHITE);
					canvas.drawLine((px + backgroundScale * i) - 2, (px + backgroundScale * j) - 1, (px + backgroundScale * i) + BORDER_SIZE, (px + backgroundScale * j) - 1, paint);
					canvas.drawLine((px + backgroundScale * i) - 1, (px + backgroundScale * j) - 2, (px + backgroundScale * i) + 2, (px + backgroundScale * j) - 2, paint);
					canvas.drawLine((px + backgroundScale * i) - 2, (px + backgroundScale * j) + 1, (px + backgroundScale * i) + BORDER_SIZE, (px + backgroundScale * j) + 1, paint);
					canvas.drawLine((px + backgroundScale * i) - 1, (px + backgroundScale * j) + 2, (px + backgroundScale * i) + 2, (px + backgroundScale * j) + 2, paint);
				}
			}
		}
		paint.setAntiAlias(false);
		canvas.restore();

		handRegion.set(rect.right + 7, rect.top + 2, rect.right + 7 + scale, rect.top + 2 + scale * 7);

		paint.setARGB(0xFF, 0x55, 0x8b, 0xe7);
		canvas.drawLine(handRegion.left - 1, handRegion.top - 1, handRegion.right + 2, handRegion.top - 1, paint);
		canvas.drawLine(handRegion.right + 2, handRegion.top - 1, handRegion.right + 2, handRegion.bottom + 1, paint);
		canvas.drawLine(handRegion.right + 2, handRegion.bottom + 1, handRegion.left - 1, handRegion.bottom + 1, paint);

/*
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
*/
	}

	public void drawHighlighter(Canvas canvas, ScribbleTile tile, Placement placement) {
		if (placement.isEmpty()) {
			return;
		}

		int x = placement.x;
		int y = placement.y;
		switch (placement.place) {
			case BOARD:
				y = boardRegion.top + scale * y;
				x = boardRegion.left + scale * x;
				break;
			case HAND:
				y = handRegion.top + scale * x;
				x = handRegion.left;
				break;
		}
		tileSurface.drawHighlighter(canvas, tile, x - 1, y - 1, scale);
	}

	public void drawScribbleTile(Canvas canvas, ScribbleTile tile, Placement placement, boolean selected, boolean pinned) {
		int x = placement.x;
		int y = placement.y;
		switch (placement.place) {
			case BOARD:
				y = boardRegion.top + scale * y;
				x = boardRegion.left + scale * x;
				break;
			case HAND:
				y = handRegion.top + scale * x;
				x = handRegion.left;
				break;
		}
		tileSurface.drawScribbleTile(canvas, tile, x - 1, y - 1, scale, selected, pinned);
	}

	public Placement getPlacement(int x, int y) {
		if (handRegion.contains(x, y)) {
			final int x1 = (y - handRegion.top) / scale;
			if (x1 < 0 || x1 > 6) {
				return null;
			}
			return new Placement(x1, 0, Place.HAND);
		}

		if (boardRegion.contains(x, y)) {
			final int x1 = (x - boardRegion.left) / scale;
			final int y1 = (y - boardRegion.top) / scale;
			if (x1 < 0 || y1 < 0 || x1 > 14 || y1 > 15) {
				return null;
			}
			return new Placement(x1, y1, Place.BOARD);
		}
		return null;
	}

	public void fillRelativePosition(Placement placement, Point position) {
		switch (placement.place) {
			case HAND:
				position.set(handRegion.left, handRegion.top + placement.x * scale);
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


		@Override
		public String toString() {
			return "Placement{" + place + "@" + x + "," + y + '}';
		}
	}
}
