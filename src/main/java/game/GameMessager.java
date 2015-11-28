package game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by ilya on 28.11.15.
 */
public class GameMessager {
    @NotNull @SuppressWarnings("ConstantConditions")
    static final Logger LOGGER = LogManager.getLogger(GameMessager.class);

    @NotNull
    GameMechanics gameMechanics;

    public GameMessager(@NotNull GameMechanics gameMechanics) {
        this.gameMechanics = gameMechanics;
    }

    public void reciveMessage(String myName, String data) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObj = (JSONObject) parser.parse(data);
            String status = (String)jsonObj.get("status");

            if ("increment".equals(status)) {
                gameMechanics.incrementScore(myName);
                return;
            }

            if ("message".equals(status)) {
                String message = (String)jsonObj.get("text");
                gameMechanics.textInChat(myName, message);
                return;
            }

        } catch (ParseException e) {
            LOGGER.error("parser JSONObject");
        }
    }



    @NotNull
    private String pushMessage(JSONObject json) {
        String message = json.toJSONString();
        if (message == null) {
            LOGGER.error("message == null");
            throw new NullPointerException();
        }

        return message;
    }

    @NotNull
    public String createMessageLeave(String whoLeave) {
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", "leave");
        jsonStart.put("name", whoLeave);

        return pushMessage(jsonStart);
    }

    @NotNull
    public String createMessageStartGame(@NotNull GameSession gameSession, String myName, int gameTime) {
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

        return pushMessage(jsonStart);
    }

    @NotNull
    public String createMessageIncrementScore(GameSession gameSession) {
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", "scores");

        JSONArray ar = new JSONArray();
        for (GameUser player: gameSession.getGameUsers()) {
            JSONObject obj = new JSONObject();
            obj.put("name", player.getName());
            obj.put("score", player.getScore());
            ar.add(obj);
        }
        jsonStart.put("players", ar);

        return pushMessage(jsonStart);
    }

    @NotNull
    public String createMessageGameOver(String nameWinner) {
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", "finish");
        jsonStart.put("win", nameWinner);

        return pushMessage(jsonStart);
    }

    @NotNull
    public String createMessageTextInChat(@NotNull String authorName, String text) {
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", "message");

        jsonStart.put("name", authorName);
        jsonStart.put("text", text);

        String message = jsonStart.toJSONString();
        if (message == null) {
            LOGGER.error("message == null");
            throw new NullPointerException();
        }

        return message;
    }
}
