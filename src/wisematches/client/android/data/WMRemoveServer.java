package wisematches.client.android.data;

import android.os.Parcelable;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.model.scribble.ScribbleDescriptor;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface WMRemoveServer {
	void authenticate(String username, String password, RemoteResponse<Personality> response);

	void loadActiveGames(long pid, RemoteResponse<ScribbleDescriptor> response);

	/**
	 * @author Sergey Klimenko (smklimenko@gmail.com)
	 */
	interface RemoteResponse<T> {
		void onSuccess(T data);

		void onFailure(String code, String message);
	}
}
