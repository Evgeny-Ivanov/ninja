package game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ilya on 27.10.15.
 */
public class WebSocketService {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger LOGGER = LogManager.getLogger(WebSocketService.class);

    @NotNull
    private Map<String, GameWebSocket> userSockets = new HashMap<>();

    public void addUserSocket(@NotNull GameWebSocket userSocket) {
        userSockets.put(userSocket.getMyName(), userSocket);
    }

    public void notifyAboutScores(@NotNull GameUser user) {
        GameWebSocket gameWebSocket = userSockets.get(user.getName());
        if (gameWebSocket != null) {
            gameWebSocket.sendScores(user);
        } else {
            LOGGER.error("gameWebSocket == null");
        }
    }

    public void notifyAboutMessage(@NotNull GameUser user, @NotNull String message) {
        GameWebSocket gameWebSocket = userSockets.get(user.getName());
        if (gameWebSocket != null) {
            gameWebSocket.sendMessage(user, message);
        } else {
            LOGGER.error("gameWebSocket == null");
        }
    }

    public void notifyStartGame(@NotNull GameUser user, int gameTime) {
        GameWebSocket gameWebSocket = userSockets.get(user.getName());
        if (gameWebSocket != null) {
            gameWebSocket.sendStartGame(user, gameTime);
        } else {
            LOGGER.error("gameWebSocket == null");
        }
    }

    public void notifyGameOver(@NotNull GameUser user, @NotNull String nameWinner) {
        GameWebSocket gameWebSocket = userSockets.get(user.getName());
        if (gameWebSocket != null) {
            gameWebSocket.sendGameOver(user, nameWinner);
        } else {
            LOGGER.error("gameWebSocket == null");
        }
    }
}