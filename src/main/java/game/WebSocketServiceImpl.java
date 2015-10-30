package game;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ilya on 27.10.15.
 */
public class WebSocketServiceImpl implements WebSocketService {
    private Map<String, GameWebSocket> userSockets = new HashMap<>();

    public void addUserSocket(GameWebSocket userSocket) {
        userSockets.put(userSocket.getMyName(), userSocket);
    }

    public void notifyAboutScores(GameUser user) {
        userSockets.get(user.getName()).sendScores(user);
    }

    public void notifyAboutMessage(GameUser user, String message) {
        userSockets.get(user.getName()).sendMessage(user, message);
    }

    public void notifyStartGame(GameUser user, int gameTime) {
        userSockets.get(user.getName()).sendStartGame(user, gameTime);
    }

    @Override
    public void notifyGameOver(GameUser user, String nameWinner) {
        userSockets.get(user.getName()).sendGameOver(user, nameWinner);
    }
}