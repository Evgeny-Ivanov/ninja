package game;

/**
 * Created by ilya on 27.10.15.
 */
public interface WebSocketService {
    void addUserSocket(GameWebSocket userSocket);

    void notifyAboutScores(GameUser user);

    void notifyStartGame(GameUser user, int gameTime);

    void notifyGameOver(GameUser user, String win);

    void notifyAboutMessage(GameUser gameUser, String message);
}
