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

    public void notifyAboutScore(GameUser user) {
        userSockets.get(user.getMyName()).setScore(user);
    }

    public void notifyStartGame(GameUser user) {
        userSockets.get(user.getMyName()).startGame(user);
    }

    @Override
    public void notifyGameOver(GameUser user, String nameWinner) {
        userSockets.get(user.getMyName()).gameOver(user, nameWinner);
    }
}