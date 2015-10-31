package game;

import base.GameServices;
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
    @NotNull
    private GameServices gameServices;

    public GameWebSocketCreator(@NotNull GameServices gameServices) {
        this.gameServices = gameServices;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        String sessionId = null;
        if (req != null && req.getHttpServletRequest() != null
                && req.getHttpServletRequest().getSession() != null) {
            sessionId = req.getHttpServletRequest().getSession().getId();
        } else {
            LOGGER.error("Error recive session");
        }

        String name;
        UserProfile userProfile = gameServices.getAccountService().getSessions(sessionId);

        if (userProfile != null) {
            name = userProfile.getName();
        } else {
            LOGGER.error("No userprofile");
            name = "";
        }

        return new GameWebSocket(name, gameServices.getGameMechanics(), gameServices.getWebSocketService());
    }
}
