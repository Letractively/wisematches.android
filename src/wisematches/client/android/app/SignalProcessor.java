package wisematches.client.android.app;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface SignalProcessor<T> {
	void onResult(T result);
}
