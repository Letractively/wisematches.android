package wisematches.client.android.app.playground.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import wisematches.client.android.app.playground.view.theme.TileSurface;
import wisematches.client.android.data.model.scribble.ScribbleTile;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SelectedWordView extends View {
	private TileSurface tileSurface;

	private ScribbleTile[] scribbleTiles = null;

	public SelectedWordView(Context context, AttributeSet attrs) {
		super(context, attrs);
		tileSurface = new TileSurface(context.getResources());
	}

	protected void onDraw(Canvas canvas) {
		final int height = getHeight();

		if (scribbleTiles != null) {
			for (int i = 0, scribbleTilesLength = scribbleTiles.length; i < scribbleTilesLength; i++) {
				ScribbleTile tile = scribbleTiles[i];
				tileSurface.drawScribbleTile(canvas, tile, height * i, 0, height, false, false);
			}
		}
	}

	public void setScribbleTiles(ScribbleTile[] scribbleTiles) {
		this.scribbleTiles = scribbleTiles;
		invalidate();
	}
}