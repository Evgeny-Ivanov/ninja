package game;

/**
 * Created by ilya on 27.10.15.
 */
public interface WebSocketService {
    void addUserSocket(GameWebSocket userSocket);

    void notifyAboutScore(GameUser user);

    void notifyStartGame(GameUser user);

    void notifyGameOver(GameUser user, String win);
}
