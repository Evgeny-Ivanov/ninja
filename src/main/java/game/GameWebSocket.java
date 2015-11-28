package game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * Created by ilya on 27.10.15.
 */

@WebSocket
public class GameWebSocket {
    @NotNull @SuppressWarnings("ConstantConditions")
    static final Logger LOGGER = LogManager.getLogger(GameWebSocket.class);

    @NotNull
    private String myName;
    @NotNull
    private GameMechanics gameMechanics;
    @NotNull
    private WebSocketService webSocketService;
    private Session session;

    public GameWebSocket(@NotNull String myName,
                         @NotNull GameMechanics gameMechanics,
                         @NotNull WebSocketService webSocketService) {
        this.myName = myName;
        this.gameMechanics = gameMechanics;
        this.webSocketService = webSocketService;
    }

    @NotNull
    public String getMyName() {
        return myName;
    }

    @OnWebSocketConnect
    public void onOpen(@NotNull Session newSession) {
        this.session = newSession;
        webSocketService.addUserSocket(this);
        gameMechanics.addUser(myName);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        gameMechanics.removeUser(myName);
        webSocketService.removeSocket(myName);
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        if (data == null) {
            return;
        }
        gameMechanics.getGameMessager().reciveMessage(myName, data);
    }

    public void send(String message) {
        if (session == null) {
            LOGGER.error("session == null");
            return;
        }
        try {
            //noinspection ConstantConditions
            session.getRemote().sendString(message);
        } catch (IOException e) {
            LOGGER.error("IOException");
        }
    }
}
