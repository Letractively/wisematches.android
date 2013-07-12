package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import wisematches.client.android.app.playground.model.ScribbleController;
import wisematches.client.android.app.playground.model.ScribbleWidget;
import wisematches.client.android.app.playground.model.SelectionListener;
import wisematches.client.android.app.playground.view.theme.BoardSurface;
import wisematches.client.android.data.model.scribble.*;
import wisematches.client.android.graphics.Dimension;

import java.util.*;

import static wisematches.client.android.app.playground.view.theme.BoardSurface.Place;
import static wisematches.client.android.app.playground.view.theme.BoardSurface.Placement;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardWidget extends FrameLayout implements ScribbleWidget {
	private Bitmap boardBackground;
	private BoardSurface boardSurface;

	private ScribbleController controller;

	private ScribbleTile draggingTile = null;
	private final Point draggingOffset = new Point();
	private final Placement draggingAnchor = new Placement();
	private final Placement draggingPosition = new Placement();
	private final Placement draggingHighlighter = new Placement();

	private final Placement reusablePlacement = new Placement();

	private final ScribbleTile[] handTiles = new ScribbleTile[7];
	private final ScribbleTile[][] boardTiles = new ScribbleTile[15][15];

	private final Map<ScribbleTile, Placement> placedTiles = new HashMap<>();

	private final TheSelectionListener selectionListener = new TheSelectionListener();

	public BoardWidget(Context context, AttributeSet attrs) {
		super(context, attrs);

		setWillNotDraw(false);
		setFocusable(true);
		setFocusableInTouchMode(true);
		setOnTouchListener(new TheOnTouchListener());

		boardSurface = new BoardSurface(context.getResources());
	}

	@Override
	public void controllerInitialized(ScribbleController controller) {
		this.controller = controller;

		controller.addSelectionListener(selectionListener);

		Arrays.fill(handTiles, null);
		for (ScribbleTile[] pinnedTile : boardTiles) {
			Arrays.fill(pinnedTile, null);
		}

		for (ScribbleMove move : controller.getScribbleBoard().getMoves()) {
			if (move instanceof ScribbleMove.Make) {
				final ScribbleMove.Make make = (ScribbleMove.Make) move;

				final ScribbleWord word = make.getWord();

				int row = word.getRow();
				int col = word.getColumn();
				WordDirection direction = word.getDirection();

				final ScribbleTile[] selectedTiles = word.getTiles();
				for (ScribbleTile tile : selectedTiles) {
					if (boardTiles[row][col] == null) {
						boardTiles[row][col] = tile;
					}

					if (direction == WordDirection.HORIZONTAL) {
						col++;
					} else {
						row++;
					}
				}
			}
		}

		final ScribbleTile[] ht = controller.getScribbleBoard().getHandTiles();
		System.arraycopy(ht, 0, this.handTiles, 0, ht.length);

		invalidateBackground();
	}

	@Override
	public void controllerTerminated(ScribbleController controller) {
		controller.removeSelectionListener(selectionListener);

		this.controller = null;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (boardBackground == null) {
			return;
		}

		canvas.drawBitmap(boardBackground, 0, 0, null);

		for (int i = 0, handTilesLength = handTiles.length; i < handTilesLength; i++) {
			final ScribbleTile tile = handTiles[i];
			if (tile != null) {
				reusablePlacement.set(i, 0, Place.HAND);
				boardSurface.drawScribbleTile(canvas, tile, reusablePlacement, false, false);
			}
		}

		if (!draggingHighlighter.isEmpty()) {
			boardSurface.drawHighlighter(canvas, draggingTile, draggingHighlighter);
		}

		for (Map.Entry<ScribbleTile, Placement> entry : placedTiles.entrySet()) {
			final ScribbleTile key = entry.getKey();
			final Placement value = entry.getValue();
			boardSurface.drawScribbleTile(canvas, key, value, true, getBoardTile(value) != null);
		}

		if (draggingTile != null) {
			boardSurface.drawScribbleTile(canvas, draggingTile, draggingPosition, true, false);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
		final int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

		if (parentHeight != 0) {
			final Dimension dimension = boardSurface.initialize(parentWidth, parentHeight);
			setMeasuredDimension(dimension.width, dimension.height);
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
		invalidateBackground();
	}

	private void invalidateBackground() {
		boardBackground = null;

		final int width = getMeasuredWidth();
		final int height = getMeasuredHeight();

		if (height == 0 || width == 0 || controller == null || boardSurface == null) {
			return;
		}

		if (boardBackground != null && boardBackground.getWidth() == width && boardBackground.getHeight() == height) {
			return;
		}

		boardBackground = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		final Canvas canvas = new Canvas(boardBackground);
		boardSurface.drawBackground(canvas, controller.getScribbleBoard().getScoreEngine());

		for (int row = 0; row < boardTiles.length; row++) {
			final ScribbleTile[] pinnedTile = boardTiles[row];
			for (int col = 0; col < pinnedTile.length; col++) {
				final ScribbleTile tile = pinnedTile[col];
				if (tile != null) {
					reusablePlacement.set(col, row, Place.BOARD);
					boardSurface.drawScribbleTile(canvas, tile, reusablePlacement, false, true);
				}
			}
		}
	}

	private void updateTileState(ScribbleTile tile, Placement placement) {
		if (placement == null) {
			placedTiles.remove(tile);
		} else {
			placedTiles.put(tile, placement);
		}

		selectionListener.selectionChangeStarted();
		try {
			controller.selectWord(getSelectedWord());
		} finally {
			selectionListener.selectionChangeFinished();
		}
	}


	private void clearSelection() {
		for (Map.Entry<ScribbleTile, Placement> entry : placedTiles.entrySet()) {
			final Placement placement = entry.getValue();

			if (getBoardTile(placement) == null) {
				for (int i = 0, handTilesLength = handTiles.length; i < handTilesLength; i++) {
					if (handTiles[i] == null) {
						handTiles[i] = entry.getKey();
						break;
					}
				}
			}
		}
		placedTiles.clear();
	}

	private void selectWord(ScribbleWord word) {
		clearSelection();

		if (word != null) {
			for (ScribbleWord.IteratorItem item : word) {
				final int row = item.getRow();
				final int column = item.getColumn();
				final ScribbleTile tile = item.getTile();

				final ScribbleTile bt = boardTiles[row][column];
				if (bt != null) {
					if (!bt.equals(tile)) {
						clearSelection();
						return;
					}
					placedTiles.put(tile, new Placement(column, row, Place.BOARD));
				} else {
					int index = 0;
					final int handTilesLength = handTiles.length;
					for (; index < handTilesLength; index++) {
						final ScribbleTile ht = handTiles[index];
						if (ht.equals(tile)) {
							break;
						}
					}

					if (index == handTilesLength) {
						clearSelection();
						return;
					}

					placedTiles.put(handTiles[index], new Placement(column, row, Place.BOARD));
					handTiles[index] = null;
				}
			}
		}
		invalidate();
	}

	private void processActionDown(Placement placement) {
		if (placement != null) {
			if (placement.in(Place.HAND)) {
				final ScribbleTile handTile = handTiles[placement.getX()];
				if (handTile != null) {
					handTiles[placement.getX()] = null;
					beginDragging(handTile, placement);
				}
			} else {
				final ScribbleTile tile = getPlacedTile(placement);
				if (tile != null && getBoardTile(placement) == null) {
					beginDragging(tile, placement);
					updateTileState(tile, null);
				}
			}
		}
	}

	private void processActionMove(Placement placement) {
		if (draggingTile != null) {
			draggingHighlighter.set(placement);
		}
	}

	private void processActionUp(Placement placement) {
		if (draggingTile != null) {
			finishDragging(placement);
		} else if (placement != null && placement.in(Place.BOARD)) {
			final ScribbleTile tile = getBoardTile(placement);
			if (tile != null) {
				if (getPlacedTile(placement) != null) {
					updateTileState(tile, null);
				} else {
					updateTileState(tile, placement);
				}
			}
		}
	}

	private void beginDragging(ScribbleTile tile, Placement placement) {
		draggingTile = tile;
		draggingAnchor.set(placement);
		draggingHighlighter.set(placement);
	}

	private void finishDragging(Placement placement) {
		if (placement == null) {
			rollbackDragging();
		} else {
			if (placement.in(Place.HAND)) {
				final ScribbleTile handTile = handTiles[placement.getX()];
				if (handTile == null) {
					handTiles[placement.getX()] = draggingTile;
					updateTileState(draggingTile, null);
				} else {
					rollbackDragging();
				}
			} else {
				if (getBoardTile(placement) == null && getPlacedTile(placement) == null) {
					updateTileState(draggingTile, placement);
				} else {
					rollbackDragging();
				}
			}
		}

		draggingTile = null;
		draggingAnchor.clear();
		draggingHighlighter.clear();
	}

	private void rollbackDragging() {
		if (draggingAnchor.in(Place.HAND)) {
			handTiles[draggingAnchor.getX()] = draggingTile;
			updateTileState(draggingTile, null);
		} else {
			updateTileState(draggingTile, new Placement(draggingAnchor));
		}
	}


	private ScribbleTile getBoardTile(Placement placement) {
		return boardTiles[placement.getY()][placement.getX()];
	}

	private ScribbleTile getPlacedTile(Placement placement) {
		for (Map.Entry<ScribbleTile, Placement> entry : placedTiles.entrySet()) {
			final Placement value = entry.getValue();
			if (value.equals(placement)) {
				return entry.getKey();
			}
		}
		return null;
	}

	private ScribbleWord getSelectedWord() {
		if (placedTiles.size() < 2) {
			return null;
		}

		final Set<Map.Entry<ScribbleTile, Placement>> set = new TreeSet<>(new Comparator<Map.Entry<ScribbleTile, Placement>>() {
			@Override
			public int compare(Map.Entry<ScribbleTile, Placement> lhs, Map.Entry<ScribbleTile, Placement> rhs) {
				if (lhs.getValue().getX() == rhs.getValue().getX()) {
					return lhs.getValue().getY() - rhs.getValue().getY();
				}
				if (lhs.getValue().getY() == rhs.getValue().getY()) {
					return lhs.getValue().getX() - rhs.getValue().getX();
				}
				return 0;
			}
		});
		set.addAll(placedTiles.entrySet());

		if (placedTiles.size() != set.size()) { // some elements were lost - incorrect order.
			return null;
		}

		final Iterator<Map.Entry<ScribbleTile, Placement>> iterator = set.iterator();

		WordDirection direction = null;
		final Map.Entry<ScribbleTile, Placement> first = iterator.next();
		for (Placement prev = first.getValue(); iterator.hasNext(); ) {
			final Map.Entry<ScribbleTile, Placement> next = iterator.next();
			WordDirection d = null;
			final Placement np = next.getValue();
			if (prev.getX() == np.getX()) {
				if (prev.getY() + 1 != np.getY()) {
					return null;
				} else {
					d = WordDirection.HORIZONTAL;
				}
			} else if (prev.getY() == np.getY()) {
				if (prev.getX() + 1 != np.getX()) {
					return null;
				} else {
					d = WordDirection.VERTICAL;
				}
			}

			if (direction == null) {
				direction = d;
			}

			if (direction != d) {
				return null;
			}
			prev = next.getValue();
		}

		int index = 0;
		ScribbleTile[] tiles = new ScribbleTile[set.size()];
		for (Map.Entry<ScribbleTile, Placement> entry : set) {
			tiles[index++] = entry.getKey();
		}
		return new ScribbleWord(first.getValue().getY(), first.getValue().getX(), direction, tiles);
	}


	private class TheOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			final int x = (int) event.getX();
			final int y = (int) event.getY();
			final Placement placement = boardSurface.getPlacement(x, y);

			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					processActionDown(placement);
					boardSurface.fillRelativePosition(placement, draggingOffset);
					draggingOffset.set(x - draggingOffset.x, y - draggingOffset.y);
					draggingPosition.set(x - draggingOffset.x, y - draggingOffset.y, Place.RELATIVE);
					break;
				case MotionEvent.ACTION_MOVE:
					processActionMove(placement);
					draggingPosition.set(x - draggingOffset.x, y - draggingOffset.y, Place.RELATIVE);
					break;
				case MotionEvent.ACTION_UP:
					processActionUp(placement);
					draggingOffset.set(0, 0);
					draggingPosition.clear();
					break;
			}

			invalidate();
			return true;
		}
	}

	private class TheSelectionListener implements SelectionListener {
		private boolean updateSelection;

		@Override
		public void onSelectionChanged(ScribbleWord word, ScoreCalculation score) {
			if (!updateSelection) {
				selectWord(word);
			}
		}

		public void selectionChangeStarted() {
			updateSelection = true;
		}

		public void selectionChangeFinished() {
			updateSelection = false;
		}
	}
}
