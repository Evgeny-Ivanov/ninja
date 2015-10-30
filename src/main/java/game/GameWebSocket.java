package game;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.JSONObject;

import java.util.Map;

/**
 * Created by ilya on 27.10.15.
 */

@WebSocket
public class GameWebSocket {
    private String myName;
    private Session session;
    private GameMechanics gameMechanics;
    private WebSocketService webSocketService;

    public GameWebSocket(String myName, GameMechanics gameMechanics, WebSocketService webSocketService) {
        this.myName = myName;
        this.gameMechanics = gameMechanics;
        this.webSocketService = webSocketService;
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        setSession(session);
        webSocketService.addUserSocket(this);
        gameMechanics.addUser(myName);
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        gameMechanics.incrementScore(myName);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {

    }


    public void startGame(GameUser user) {
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "start");

            jsonStart.put("name", user.getMyName());

            for (Map.Entry<String, Integer> enemy: user.getEnemyNameAndScore().entrySet()) {
                jsonStart.put("name", enemy.getKey());
            }
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void gameOver(GameUser user, String nameWinner) {
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "finish");
            jsonStart.put("win", nameWinner);
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }


    public void setScore(GameUser user) {
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", "score");

        for (Map.Entry<String, Integer> enemy: user.getEnemyNameAndScore().entrySet()) {
            jsonStart.put("name", enemy.getKey());
            jsonStart.put("score", enemy.getValue());
        }

        try {
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public String getMyName() {
        return myName;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }


}
