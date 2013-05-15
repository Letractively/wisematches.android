package wisematches.client.android.data;

import android.app.Activity;
import android.content.Context;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.model.scribble.ScribbleDescriptor;

import java.util.ArrayList;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface DataRequestManager {
	void authenticate(String username, String password, DataResponse<Personality> response);

	void register(String username, String email, String password, String confirm, String language, String timezone, DataResponse<Personality> response);


	void getPersonality(long pid, DataResponse<Personality> response);

	void getActiveGames(long pid, DataResponse<ArrayList<ScribbleDescriptor>> response);


	/**
	 * @author Sergey Klimenko (smklimenko@gmail.com)
	 */
	public interface DataResponse<T> {
		/**
		 * Is executed when correct data was received from server.
		 *
		 * @param data the received data.
		 */
		void onSuccess(T data);

		/**
		 * Indicates that executed operation was rejected by an error.
		 *
		 * @param code    the error code returned by server.
		 * @param message the error message returned by server.
		 */
		void onFailure(String code, String message);


		void onDataError();

		void onConnectionError(int code);
	}
}
