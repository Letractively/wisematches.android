package wisematches.client.android.data.model.scribble;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface ScribbleWidget {
	void boardInitialized(ScribbleBoard board);

	void boardTerminated(ScribbleBoard board);
}
