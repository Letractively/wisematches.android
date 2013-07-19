package wisematches.client.android.data;

import wisematches.client.android.data.model.Id;
import wisematches.client.android.data.model.Language;
import wisematches.client.android.data.model.info.InfoPage;
import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.model.scribble.*;

import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface DataRequestManager {
	void login(String username, String password, DataResponse<Personality> response);

	void register(String nickname, String email, String password, String confirm, String language, String timezone, DataResponse<Personality> response);


	void getActiveGames(long pid, DataResponse<ActiveGames> response);

	void getWaitingGames(DataResponse<WaitingGames> response);

	void processWaitingGame(long proposalId, boolean accept, DataResponse<Id> response);


	void createBoard(String title, Language language, int timeout, String createTab, String robotType, int opponentsCount, DataResponse<Id> response);


	void openBoard(long boardId, DataResponse<ScribbleSnapshot> response);

	void validateBoard(long id, long lastChange, DataResponse<ScribbleChanges> response);

	void passTurn(long boardId, DataResponse<ScribbleChanges> response);

	void resignGame(long boardId, DataResponse<ScribbleChanges> response);

	void makeTurn(long boardId, ScribbleWord word, DataResponse<ScribbleChanges> response);

	void exchangeTiles(long boardId, Set<ScribbleTile> tiles, DataResponse<ScribbleChanges> response);


	void getWordEntry(String word, String lang, DataResponse<WordEntry> response);


	void loadInfoPage(String name, DataResponse<InfoPage> response);


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
