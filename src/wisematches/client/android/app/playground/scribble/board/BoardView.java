package wisematches.client.android.app.playground.scribble.board;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import wisematches.client.android.app.playground.scribble.board.surface.BoardSurface;
import wisematches.client.android.app.playground.scribble.board.surface.TileSurface;
import wisematches.client.android.data.model.scribble.*;
import wisematches.client.android.graphics.Dimension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardView extends FrameLayout {
	private ScribbleBoard board;

	private Position draggingAnchor = null;
	private TileSurface draggingTile = null;

	private Position highlightPosition = null;

	private ScribbleWord selectedWord = null;
	private final Set<ScribbleTile> selectedTiles = new HashSet<>();

	private final Point draggingOffset = new Point();
	private final Point touchingPosition = new Point();
	private final Point draggingPosition = new Point();

	private final BoardSurface boardSurface;
	private final TileSurface[] handTileSurfaces = new TileSurface[7];
	private final TileSurface[][] boardTileSurfaces = new TileSurface[15][15];

	private final List<BoardViewListener> listeners = new ArrayList<>();

	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setWillNotDraw(false);
		setFocusable(true);
		setFocusableInTouchMode(true);
		setOnTouchListener(new TheOnTouchListener());

		boardSurface = new BoardSurface(context.getResources());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
		final int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

		final Dimension dimension = boardSurface.onMeasure(parentWidth, parentHeight);
		if (dimension != null) {
			setMeasuredDimension(dimension.width, dimension.height);
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}


	public void initBoardView(ScribbleBoard board) {
		this.board = board;

		this.boardSurface.initScoreEngine(board.getScoreEngine());

		for (ScribbleMove move : board.getMoves()) {
			if (move instanceof ScribbleMove.Make) {
				final ScribbleMove.Make make = (ScribbleMove.Make) move;

				final ScribbleWord word = make.getWord();

				int row = word.getRow();
				int col = word.getColumn();
				WordDirection direction = word.getDirection();

				final ScribbleTile[] selectedTiles = word.getTiles();
				for (ScribbleTile tile : selectedTiles) {
					if (boardTileSurfaces[row][col] == null) {
						boardTileSurfaces[row][col] = new TileSurface(tile, true, null);
					}

					if (direction == WordDirection.HORIZONTAL) {
						col++;
					} else {
						row++;
					}
				}
			}
		}

		ScribbleTile[] handTiles = board.getHandTiles();
		if (handTiles != null) {
			for (int i = 0; i < handTiles.length; i++) {
				ScribbleTile handTile = handTiles[i];
				if (handTile != null) {
					handTileSurfaces[i] = new TileSurface(handTile, false, null);
				}
			}
		}
		invalidate();
	}

	@Deprecated
	public void setScribbleBoardListener(BoardViewListener listener) {
		this.listeners.add(listener);
	}

	@Deprecated
	public ScribbleBoard getBoard() {
		return board;
	}


	public ScribbleWord getSelectedWord() {
		return selectedWord;
	}

	public void setSelectWord(ScribbleWord word) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}


	public Set<ScribbleTile> getSelectedTiles() {
		return selectedTiles;
	}

	public void clearSelection() {
		for (int i = 0; i < boardTileSurfaces.length; i++) {
			for (int j = 0; j < boardTileSurfaces.length; j++) {
				TileSurface tileSurface = boardTileSurfaces[i][j];
				if (tileSurface != null && tileSurface.isSelected()) {
					if (!tileSurface.isPinned()) {
						boardTileSurfaces[i][j] = null;
						for (int h = 0; h < handTileSurfaces.length; h++) {
							if (handTileSurfaces[h] == null) {
								handTileSurfaces[h] = tileSurface;
								break;
							}
						}
					}
					changeTileSelected(tileSurface, false);
				}
			}
		}
		invalidate();
	}


	protected void processTouchEvent(MotionEvent event) {
		final int x = (int) event.getX();
		final int y = (int) event.getY();

		draggingPosition.set(x, y);

		final int scale = boardSurface.getScale();
		final Position tilePosition = getTilePosition(x, y);
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (tilePosition != null) {
					if (tilePosition.hand) {
						if (tilePosition.row == 0 && handTileSurfaces[tilePosition.col] != null) {
							beginDragging(handTileSurfaces[tilePosition.col], tilePosition);
							handTileSurfaces[tilePosition.col] = null;
						}
					} else {
						TileSurface tileSurface = boardTileSurfaces[tilePosition.row][tilePosition.col];
						if (tileSurface != null && !tileSurface.isPinned()) {
							beginDragging(tileSurface, tilePosition);
							boardTileSurfaces[tilePosition.row][tilePosition.col] = null;
						}
					}
					draggingOffset.set(x - tilePosition.rect.left - tilePosition.col * scale, y - tilePosition.rect.top - tilePosition.row * scale);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (draggingTile != null) {
					highlightPosition = tilePosition;
				}
				break;
			case MotionEvent.ACTION_UP:
				if (draggingTile != null) {
					finishDragging(tilePosition);
				} else if (tilePosition != null && !tilePosition.hand) {
					final TileSurface tileSurface = boardTileSurfaces[tilePosition.row][tilePosition.col];
					if (tileSurface != null) {
						if (tileSurface.isSelected()) {
							changeTileSelected(tileSurface, false);
						} else {
							changeTileSelected(tileSurface, true);
						}
					} else {
//						clearSelection();
					}
				}
				break;
		}
		invalidate();
	}

	protected void changeTileSelected(TileSurface tileSurface, boolean selected) {
		tileSurface.setSelected(selected);

		if (selected) {
			selectedTiles.add(tileSurface.getTile());
		} else {
			selectedTiles.remove(tileSurface.getTile());
		}

		for (BoardViewListener selectionListener : listeners) {
			selectionListener.onTileSelected(tileSurface.getTile(), selected, selectedTiles);
		}

		Position position = null;
		ScribbleWord word = null;
		WordDirection direction = null;
		List<ScribbleTile> wordTiles = new ArrayList<>();
		for (int row = 0; row < boardTileSurfaces.length && wordTiles != null; row++) {
			for (int col = 0; col < boardTileSurfaces.length && wordTiles != null; col++) {
				TileSurface tile = boardTileSurfaces[row][col];
				if (tile != null && tile.isSelected()) {
					if (wordTiles.size() == 0) {
						position = new Position(row, col, null, false);
					} else if (wordTiles.size() == 1) {
						if (position.row + wordTiles.size() == row && position.col == col) {
							direction = WordDirection.VERTICAL;
						} else if (position.col + wordTiles.size() == col && position.row == row) {
							direction = WordDirection.HORIZONTAL;
						} else {
							wordTiles = null;
						}
					} else {
						if (direction == WordDirection.VERTICAL) {
							if (position.row + wordTiles.size() != row || position.col != col) {
								wordTiles = null;
							}
						} else {
							if (position.col + wordTiles.size() != col || position.row != row) {
								wordTiles = null;
							}
						}
					}

					if (wordTiles != null) {
						wordTiles.add(tile.getTile());
					}
				}
			}
		}

		if (direction != null && position != null && wordTiles != null) {
			word = new ScribbleWord(position.row, position.col, direction, wordTiles.toArray(new ScribbleTile[wordTiles.size()]));
		}

		if (word != null) {
			selectedWord = word;
			for (BoardViewListener selectionListener : listeners) {
				selectionListener.onWordSelected(selectedWord);
			}
		} else if (selectedWord != null) {
			selectedWord = null;
			for (BoardViewListener selectionListener : listeners) {
				selectionListener.onWordSelected(selectedWord);
			}
		}
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (boardSurface != null) {
			boardSurface.drawBackground(canvas);

//			final int scale = boardSurface.getScale();
//
//			final Rect handRegion = boardSurface.getHandRegion();
//			final Rect boardRegion = boardSurface.getBoardRegion();

/*
			if (draggingTile != null && highlightPosition != null) {
				Rect r = highlightPosition.rect;
				boardSurface.drawHighlighter(draggingTile, r.left + highlightPosition.col * scale, r.top + highlightPosition.row * scale);
			}

			for (int row = 0; row < boardTileSurfaces.length; row++) {
				TileSurface[] boardTileSurface = boardTileSurfaces[row];
				for (int col = 0; col < boardTileSurface.length; col++) {
					TileSurface tileSurface = boardTileSurface[col];
					if (tileSurface != null) {
						tileSurface.onDraw(canvas, boardRegion.left + col * scale, boardRegion.top + row * scale, scale);
					}
				}
			}

			for (int i = 0; i < handTileSurfaces.length; i++) {
				TileSurface handTileSurface = handTileSurfaces[i];
				if (handTileSurface != null) {
					handTileSurface.onDraw(canvas, handRegion.left + i * scale, handRegion.top, scale);
				}
			}

			if (draggingTile != null) {
				draggingTile.onDraw(canvas, draggingPosition.x - draggingOffset.x, draggingPosition.y - draggingOffset.y, scale);
			}
*/
		}
	}


	private void beginDragging(TileSurface tileSurface, Position tilePosition) {
		draggingTile = tileSurface;

		draggingTile.setSelected(true);
		draggingAnchor = highlightPosition = tilePosition;
	}

	private void finishDragging(Position tilePosition) {
		if (tilePosition == null) {
			rollbackDragging();
		} else {
			if (tilePosition.hand) {
				final TileSurface tileSurface = handTileSurfaces[tilePosition.col];
				if (tileSurface == null) {
					handTileSurfaces[tilePosition.col] = draggingTile;
					changeTileSelected(draggingTile, false);
				} else {
					rollbackDragging();
				}
			} else {
				final TileSurface tileSurface = boardTileSurfaces[tilePosition.row][tilePosition.col];
				if (tileSurface == null) {
					boardTileSurfaces[tilePosition.row][tilePosition.col] = draggingTile;
					changeTileSelected(draggingTile, true);
				} else {
					rollbackDragging();
				}
			}
		}

		draggingTile = null;
		draggingAnchor = null;

		highlightPosition = null;
	}

	private void rollbackDragging() {
		if (draggingAnchor.hand) {
			draggingTile.setSelected(false);
			handTileSurfaces[draggingAnchor.col] = draggingTile;
		} else {
			boardTileSurfaces[draggingAnchor.row][draggingAnchor.col] = draggingTile;
		}
	}


	private Position getTilePosition(int x, int y) {
		final int scale = boardSurface.getScale();
		Rect region;
		region = boardSurface.getBoardRegion();
		if (region.contains(x, y)) {
			final int row = (y - region.top) / scale;
			final int col = (x - region.left) / scale;
			return new Position(row, col, region, false);
		}

		region = boardSurface.getHandRegion();
		if (region.contains(x, y)) {
			final int row = (y - region.top) / scale;
			final int col = (x - region.left) / scale;
			return new Position(row, col, region, true);
		}
		return null;
	}

	@Deprecated
	private static class Position {
		public final int row;
		public final int col;
		public final Rect rect;
		public final boolean hand;

		public Position(int row, int col, Rect rect, boolean hand) {
			this.row = row;
			this.col = col;
			this.rect = rect;
			this.hand = hand;
		}
	}

	private class TheOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			processTouchEvent(motionEvent);
			return true;
		}
	}
}