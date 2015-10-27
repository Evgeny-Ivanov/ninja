package game;

import base.AccountService;
import base.GameServices;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

/**
 * Created by ilya on 27.10.15.
 */
public class GameWebSocketCreator implements WebSocketCreator {
    private GameServices gameServices;

    public GameWebSocketCreator(GameServices gameServices) {
        this.gameServices = gameServices;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        String sessionId = req.getHttpServletRequest().getSession().getId();
        String name = gameServices.getAccountService().getSessions(sessionId).getName();
        return new GameWebSocket(name, gameServices.getGameMechanics(), gameServices.getWebSocketService());
    }
}
