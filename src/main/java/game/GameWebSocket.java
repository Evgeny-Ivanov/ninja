package game;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObj = (JSONObject) parser.parse(data);
            Object status = jsonObj.get("paramsStr");

            if ("increment".equals(status)) {
                gameMechanics.incrementScore(myName);
            } else if ("message".equals(status)) {
                Object message = jsonObj.get("text");
                gameMechanics.messageInChat(myName, message.toString());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {

    }


    public void sendStartGame(GameUser user, int gameTime) {
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "start");
            jsonStart.put("your_name", user.getName());
            jsonStart.put("time_of_game", (int)(gameTime / 1000));

            JSONArray ar = new JSONArray();
            for (GameUser player: user.getPlayersGameUsers()) {
                JSONObject obj = new JSONObject();
                obj.put("name", player.getName());
                ar.add(obj);
            }
            jsonStart.put("players", ar);


            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void sendGameOver(GameUser user, String nameWinner) {
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "finish");
            jsonStart.put("win", nameWinner);
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }


    public void sendScores(GameUser user) {
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", "scores");

        JSONArray ar = new JSONArray();
        for (GameUser player: user.getPlayersGameUsers()) {
            JSONObject obj = new JSONObject();
            obj.put("name", player.getName());
            obj.put("score", player.getScore());
            ar.add(obj);
        }
        jsonStart.put("players", ar);

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


    public void sendMessage(GameUser user, String message) {
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", "message");

        for (GameUser player: user.getPlayersGameUsers()) {
            jsonStart.put("name", player.getName());
            jsonStart.put("message", message);
        }

        try {
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
}
