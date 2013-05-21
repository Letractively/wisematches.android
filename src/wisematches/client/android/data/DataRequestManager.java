package wisematches.client.android.data;

import wisematches.client.android.data.model.Id;
import wisematches.client.android.data.model.Language;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.model.scribble.ScribbleDescriptor;

import java.util.ArrayList;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface DataRequestManager {
	void register(String nickname, String email, String password, String confirm, String language, String timezone, DataResponse<Personality> response);

	void authenticate(String username, String password, DataResponse<Personality> response);


	void getActiveGames(long pid, DataResponse<ArrayList<ScribbleDescriptor>> response);

	void createNewGame(String title, Language language, int timeout, String createTab, String robotType, int opponentsCount, DataResponse<Id> response);


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
