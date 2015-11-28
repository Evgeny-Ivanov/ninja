package game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
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

    public boolean removeSocket(@NotNull String name) {
        return userSockets.remove(name) != null;
    }

    public void notify(@NotNull String userName, @NotNull String message) {
        GameWebSocket gameWebSocket = userSockets.get(userName);
        if (gameWebSocket == null) {
            LOGGER.error("gameWebSocket == null");
            return;
        }

        gameWebSocket.send(message);
    }

    public void notifyAboutLeave(@NotNull String nameUser, @NotNull String whoLeave) {
        GameWebSocket gameWebSocket = userSockets.get(nameUser);
        if (gameWebSocket != null) {
            gameWebSocket.sendLeave(nameUser, whoLeave);
        } else {
            LOGGER.error("gameWebSocket == null");
        }
    }

    public void notifyStartGame(@NotNull String userName, @NotNull GameSession gameSession, int gameTime) {
        GameWebSocket gameWebSocket = userSockets.get(userName);
        if (gameWebSocket != null) {
            gameWebSocket.sendStartGame(gameSession, gameTime);
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