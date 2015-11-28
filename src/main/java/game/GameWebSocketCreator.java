package game;

import base.AccountService;
import base.AccountServiceImpl;
import base.GameContext;
import base.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ilya on 27.10.15.
 */
public class GameWebSocketCreator implements WebSocketCreator {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger LOGGER = LogManager.getLogger(GameWebSocketCreator.class);


    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        @SuppressWarnings("ConstantConditions")
        String sessionId = req.getHttpServletRequest().getSession().getId();

        GameContext gameContext = GameContext.getInstance();

        AccountService accountService = (AccountService) gameContext.get(AccountService.class);
        if (accountService == null) {
            LOGGER.error("accountService == null");
            throw new NullPointerException();
        }

        GameMechanics gameMechanics = (GameMechanics) gameContext.get(GameMechanics.class);
        if (gameMechanics == null) {
            LOGGER.error("gameMechanics == null");
            throw new NullPointerException();
        }

        WebSocketService webSocketService = (WebSocketService) gameContext.get(WebSocketService.class);
        if (webSocketService == null) {
            LOGGER.error("webSocketService == null");
            throw new NullPointerException();
        }

        UserProfile userProfile = accountService.getSessions(sessionId);
        if (userProfile == null) {
            LOGGER.error("userProfile == null");
            throw new NullPointerException();
        }

        return new GameWebSocket(userProfile.getName(), gameMechanics, webSocketService);
    }
}
