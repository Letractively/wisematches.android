package wisematches.client.android.app.playground.scribble.board.surface;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import wisematches.client.android.data.model.scribble.ScribbleTile;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
public class TileSurface {
	private boolean pinned = false;
	private boolean selected = false;

	private ScribbleTile tile;
	private final BitmapFactory bitmapFactory;

	private static final double MAGIC_COEF = 0.7;

	public TileSurface(ScribbleTile tile, boolean pinned, BitmapFactory bitmapFactory) {
		this.tile = tile;
		this.pinned = pinned;
		this.bitmapFactory = bitmapFactory;
	}

	public void pin() {
		pinned = true;
	}

	public boolean isPinned() {
		return pinned;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}


	public Bitmap getSurface() {
		return null;
	}

	public Bitmap getHighlighter() {
		return null;
	}


	public ScribbleTile getTile() {
		return tile;
	}


	public void onDraw(Canvas canvas) {
	}

	@Deprecated
	public void onDraw(Canvas canvas, int x, int y, int size) {
		final Paint paint = new Paint();
		paint.setTextSize((float) (size * MAGIC_COEF));
		paint.setTextAlign(Paint.Align.CENTER);

		Bitmap bitmap;
		if (pinned) {
			if (selected) {
				bitmap = Bitmap.createScaledBitmap(bitmapFactory.getTilePinnedSelectedIcon(tile.getCost()), size, size, false);
			} else {
				bitmap = bitmapFactory.getTilePinnedIcon(tile.getCost());
			}
		} else {
			if (selected) {
				bitmap = bitmapFactory.getTileSelectedIcon(tile.getCost());
			} else {
				bitmap = bitmapFactory.getTileIcon(tile.getCost());
			}
		}
		paint.setFakeBoldText(selected);

		bitmap = Bitmap.createScaledBitmap(bitmap, size, size, false);
		canvas.drawBitmap(bitmap, x, y, null);

		paint.setAntiAlias(true);
		if (tile.isWildcard()) {
			paint.setColor(Color.BLACK);
		} else {
			paint.setColor(Color.WHITE);
		}

		canvas.drawText(tile.getLetter(), x + size / 2, y + (size - paint.ascent()) / 2 - 1, paint);
		paint.setAntiAlias(false);
	}
}
