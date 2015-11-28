package game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * Created by ilya on 27.10.15.
 */

@WebSocket
public class GameWebSocket {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger LOGGER = LogManager.getLogger(GameWebSocket.class);

    @NotNull
    private String myName;
    @NotNull
    private Session session;
    @NotNull
    private GameMechanics gameMechanics;
    @NotNull
    private WebSocketService webSocketService;

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

    @SuppressWarnings("ParameterHidesMemberVariable")
    @OnWebSocketConnect
    public void onOpen(@NotNull Session session) {
        this.session = session;
        webSocketService.addUserSocket(this);
        gameMechanics.addUser(myName);
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObj = (JSONObject) parser.parse(data);
            String status = (String)jsonObj.get("status");

            if ("increment".equals(status)) {
                gameMechanics.incrementScore(myName);
            } else if ("message".equals(status)) {
                String message = (String)jsonObj.get("text");
                gameMechanics.textInChat(myName, message);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        gameMechanics.removeUser(myName);
        webSocketService.removeSocket(myName);
    }


    public void send(String message) {
        try {
            //noinspection ConstantConditions
            session.getRemote().sendString(message);
        } catch (IOException e) {
            LOGGER.error("IOException");
        }
    }

    public void sendStartGame(@NotNull GameSession gameSession, int gameTime) {
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", "start");
        jsonStart.put("your_name", myName);
        jsonStart.put("time_of_game", gameTime / 1000);

        JSONArray ar = new JSONArray();
        for (GameUser player: gameSession.getGameUsers()) {
            JSONObject obj = new JSONObject();
            obj.put("name", player.getName());
            ar.add(obj);
        }
        jsonStart.put("players", ar);

        try {
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendGameOver(GameUser user, String nameWinner) {
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", "finish");
        jsonStart.put("win", nameWinner);
        try {
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendLeave(String nameUser, String whoLeave) {
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", "leave");
        jsonStart.put("name", whoLeave);

        try {
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
