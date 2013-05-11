package wisematches.client.android.data;

import wisematches.client.android.data.model.person.Personality;
import wisematches.client.android.data.model.scribble.ScribbleDescriptor;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RemoteServer {
    void authenticate(String username, String password, RemoteResponse<Personality> response);


    void getPersonality(long pid, RemoteResponse<Personality> response);

    void getActiveGames(long pid, RemoteResponse<ScribbleDescriptor> response);


    /**
     * @author Sergey Klimenko (smklimenko@gmail.com)
     */
    interface RemoteResponse<T> {
        void onSuccess(T data);

        void onFailure(String code, String message);
    }
}
