package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import wisematches.client.android.R;
import wisematches.client.android.app.playground.model.ScribbleController;
import wisematches.client.android.app.playground.model.ScribbleWidget;
import wisematches.client.android.data.model.scribble.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardView extends FrameLayout implements ScribbleWidget {

	private ScribbleController controller;

	private int scale = 0;
	private Bitmap boardBackground;

	private final Rect handRegion = new Rect();
	private final Rect boardRegion = new Rect();

	private final ScribbleTile[] handTiles = new ScribbleTile[7];
	private final ScribbleTile[][] pinnedTiles = new ScribbleTile[15][15];

	private static final int MAGIC_COEF = 8;
	private static final int BORDER_SIZE = 3;
	private static final float MAGIC_TEXT_COEF = 0.6f;

	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setWillNotDraw(false);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	@Override
	public void controllerInitialized(ScribbleController controller) {
		this.controller = controller;

		Arrays.fill(handTiles, null);
		for (ScribbleTile[] pinnedTile : pinnedTiles) {
			Arrays.fill(pinnedTile, null);
		}

		final List<ScribbleMove> moves = controller.getScribbleMoves();

		for (ScribbleMove move : moves) {
			if (move instanceof ScribbleMove.Make) {
				final ScribbleMove.Make make = (ScribbleMove.Make) move;

				final ScribbleWord word = make.getWord();

				int row = word.getRow();
				int col = word.getColumn();
				WordDirection direction = word.getDirection();

				final ScribbleTile[] selectedTiles = word.getTiles();
				for (ScribbleTile tile : selectedTiles) {
					if (pinnedTiles[row][col] == null) {
						pinnedTiles[row][col] = tile;
					}

					if (direction == WordDirection.HORIZONTAL) {
						col++;
					} else {
						row++;
					}
				}
			}
		}

		invalidateBackground();
/*
		ScribbleTile[] handTiles = board.getHandTiles();
		if (handTiles != null) {
			for (int i = 0; i < handTiles.length; i++) {
				ScribbleTile handTile = handTiles[i];
				if (handTile != null) {
//					handTileSurfaces[i] = new TileSurface(handTile, false, null);
				}
			}
		}
*/
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

		if (parentHeight != 0) {
			scale = (parentHeight - 8) / 17;
			if (scale % 2 != 0) {
				scale -= 1;
			}
			final int width = (BORDER_SIZE + scale / 2) * 2 + scale * 15;
			final int height = BORDER_SIZE + scale / 2 + scale * 15 + scale + BORDER_SIZE;
			setMeasuredDimension(width, height);
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
		invalidateBackground();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (boardBackground == null) {
			return;
		}

		canvas.drawBitmap(boardBackground, 0, 0, null);
	}


	@Override
	public void controllerTerminated(ScribbleController controller) {
		this.controller = null;
	}

	private void invalidateBackground() {
		boardBackground = null;

		final int width = getMeasuredWidth();
		final int height = getMeasuredHeight();

		if (height == 0 || width == 0) {
			return;
		}

		if (boardBackground != null && boardBackground.getWidth() == width && boardBackground.getHeight() == height) {
			return;
		}

		boardBackground = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		final Canvas canvas = new Canvas(boardBackground);
		drawBoardBackground(canvas);

		for (int row = 0; row < pinnedTiles.length; row++) {
			final ScribbleTile[] pinnedTile = pinnedTiles[row];
			for (int col = 0; col < pinnedTile.length; col++) {
				final ScribbleTile tile = pinnedTile[col];
				if (tile != null) {
					drawScribbleTile(canvas, tile, col, row, Place.BOARD, false, true);
				}
			}
		}
	}

	private void drawScribbleTile(Canvas canvas, ScribbleTile tile, int x, int y, Place place, boolean selected, boolean pinned) {
		Bitmap bitmap;
		final BitmapFactory bitmapFactory = BitmapFactory.getBitmapFactory(getContext());
		if (selected && pinned) {
			bitmap = bitmapFactory.getTilePinnedSelectedIcon(tile.getCost());
		} else if (selected) {
			bitmap = bitmapFactory.getTileSelectedIcon(tile.getCost());
		} else if (pinned) {
			bitmap = bitmapFactory.getTilePinnedIcon(tile.getCost());
		} else {
			bitmap = bitmapFactory.getTileIcon(tile.getCost());
		}

		final Paint paint = new Paint();
		paint.setTextSize(scale * MAGIC_TEXT_COEF);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setFakeBoldText(selected);

		bitmap = Bitmap.createScaledBitmap(bitmap, scale, scale, false);

		switch (place) {
			case BOARD:
				y = boardRegion.top + scale * y;
				x = boardRegion.left + scale * x;
				break;
			case HAND:
				y = 0;
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

	private void drawBoardBackground(Canvas canvas) {
		final int border = scale / 2;
		final ScoreEngine scoreEngine = controller.getScoreEngine();
		final Resources resources = getResources();
		final ScoreBonus.Type[] values = ScoreBonus.Type.values();
		final String[] bonusCaptions = new String[values.length];
		final String boardCaption = resources.getString(R.string.board_surface_captions);
		for (int i = 0; i < values.length; i++) {
			final ScoreBonus.Type value = values[i];
			try {
				bonusCaptions[i] = resources.getString(resources.getIdentifier("board_surface_bonus_" + value.name(), "string", "wisematches.client.android"));
			} catch (Exception ignore) {
			}
		}

		final Paint paint = new Paint();
		paint.setStrokeWidth(1f);

		final Rect rect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
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

	private static enum Place {
		HAND,
		BOARD,
		RELATIVE
	}
}
