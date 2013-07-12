package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.View;
import wisematches.client.android.R;
import wisematches.client.android.app.playground.model.ScribbleController;
import wisematches.client.android.data.model.scribble.ScribbleBank;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ProgressView extends View {
	private int handTiles = 0;
	private int boardTiles = 0;
	private int totalTiles = 0;

	private String finalizationMessage;
	private State state = State.WAITING;

	private final Clr handColor;
	private final Clr bankColor;
	private final Clr boardColor;
	private final Clr finishedColor;

	private final float densityMultiplier;

	private final Rect handRect = new Rect(0, 0, 0, 1);
	private final Rect bankRect = new Rect(0, 0, 0, 1);
	private final Rect boardRect = new Rect(0, 0, 0, 1);


	private static final int RADII = 8;

	private static final ShapeDrawable NONE = new ShapeDrawable(new RectShape());
	private static final ShapeDrawable LEFT = new ShapeDrawable(new RoundRectShape(new float[]{RADII, RADII, 0, 0, 0, 0, RADII, RADII}, null, null));
	private static final ShapeDrawable RIGHT = new ShapeDrawable(new RoundRectShape(new float[]{0, 0, RADII, RADII, RADII, RADII, 0, 0}, null, null));
	private static final ShapeDrawable ALL = new ShapeDrawable(new RoundRectShape(new float[]{RADII, RADII, RADII, RADII, RADII, RADII, RADII, RADII}, null, null));

	public ProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);

		final Resources resources = context.getResources();
		densityMultiplier = resources.getDisplayMetrics().density;

		if (isInEditMode()) {
			handColor = new Clr(0xff000000, 0xffff0000);
			bankColor = new Clr(0xff000000, 0xffff0000);
			boardColor = new Clr(0xff000000, 0xffff0000);

			finishedColor = new Clr(0xff000000, 0xffff0000);
		} else {
			handColor = new Clr(resources.getColor(R.color.progress_hand_border), resources.getColor(R.color.progress_hand_background));
			bankColor = new Clr(resources.getColor(R.color.progress_bank_border), resources.getColor(R.color.progress_bank_background));
			boardColor = new Clr(resources.getColor(R.color.progress_board_border), resources.getColor(R.color.progress_board_background));

			finishedColor = new Clr(resources.getColor(R.color.progress_no_border), resources.getColor(R.color.progress_no_background));
		}
	}

	protected void onDraw(Canvas canvas) {
		final int width = getWidth();
		final int height = getHeight();

		if (state == State.FINISHED || state == State.WAITING) {
			drawPart(canvas, ALL, new Rect(1, 1, width - 1, height - 1), finishedColor);

			if (state == State.WAITING) {
				drawText(canvas, width, height, "Loading board...");
			} else {
				drawText(canvas, width, height, finalizationMessage);
			}
		} else {
			final int bankTiles = totalTiles - this.boardTiles - handTiles;

			final float p3 = handTiles / (float) totalTiles; // hand
			final float p2 = boardTiles / (float) totalTiles; // board
			final float p1 = 1f - p3 - p2; // bank

			if (p2 > 0.01) {
				boardRect.set(1, 1, (int) (width * p2), height - 1);
			} else {
				boardRect.set(0, 0, 0, 0);
			}

			if (p1 > 0.01) {
				bankRect.set(boardRect.right + 1, 1, boardRect.right + (int) (width * p1), height - 1);
			} else {
				bankRect.set(0, 0, 0, 0);
			}

			if (p2 > 0.01) {
				handRect.set(bankRect.right + 1, 1, width - 1, height - 1);
			} else {
				handRect.set(0, 0, 0, 0);
			}

			if (!boardRect.isEmpty()) {
				drawPart(canvas, LEFT, boardRect, boardColor);
			}

			if (!bankRect.isEmpty()) {
				drawPart(canvas, handRect.isEmpty() ? RIGHT : NONE, bankRect, bankColor);
			}

			if (!handRect.isEmpty()) {
				drawPart(canvas, RIGHT, handRect, handColor);
			}
			drawText(canvas, width, height, this.boardTiles + "/" + bankTiles + "/" + handTiles);
		}
	}

	public void updateProgress(ScribbleController board) {
		if (board.isActive()) {
			final ScribbleBank bank = board.getScribbleBank();

			final int totalTiles = bank.getLettersCount();
			final int boardTiles = board.getBoardTilesCount();

			int k = board.getPlayers().size() * 7;
			final int handTiles = (k <= totalTiles - boardTiles) ? k : totalTiles - boardTiles;

			state = State.PROGRESS;

			this.handTiles = handTiles;
			this.boardTiles = boardTiles;
			this.totalTiles = totalTiles;
		} else {
			state = State.FINISHED;

			this.finalizationMessage = finalizationMessage;
		}
	}

	private void drawText(Canvas canvas, int width, int height, String text) {
		final Paint textPaint = new Paint();
		textPaint.setColor(0xFF666666);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setFakeBoldText(true);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(12f * densityMultiplier);

		int xPos = (width / 2);
		int yPos = (int) ((height / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));

		canvas.drawText(text, xPos, yPos, textPaint);
	}

	private void drawPart(Canvas canvas, final ShapeDrawable shape, Rect rect, final Clr color) {
		shape.setBounds(rect);

		final Paint paint = shape.getPaint();
		paint.setStrokeWidth(1);

		paint.setStyle(Paint.Style.FILL);
		paint.setColor(color.background);
		shape.draw(canvas);

		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(color.border);
		shape.draw(canvas);
	}

	private static final class Clr {
		private final int border;
		private final int background;

		private Clr(int border, int background) {
			this.border = border;
			this.background = background;
		}
	}

	public static enum State {
		WAITING,
		PROGRESS,
		FINISHED
	}
}