package game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ilya on 28.11.15.
 */
public class GameMessager {
    @NotNull
    private final GameMechanics gameMechanics;
    @NotNull
    private final JsonParser jsonParser = new JsonParser();

    public GameMessager(@NotNull GameMechanics gameMechanics) {
        this.gameMechanics = gameMechanics;
    }

    public void reciveMessage(@NotNull String myName, @NotNull String data) {
        @SuppressWarnings("ConstantConditions")
        JsonObject jsonObj = jsonParser.parse(data).getAsJsonObject();

        @SuppressWarnings("ConstantConditions")
        String status = jsonObj.get("status").getAsString();

        if ("increment".equals(status)) {
            gameMechanics.incrementScore(myName);
            return;
        }

        if ("message".equals(status)) {
            @SuppressWarnings("ConstantConditions")
            String text = jsonObj.get("text").getAsString();
            gameMechanics.textInChat(myName, text);
        }
    }

    @NotNull
    public String createMessageLeave(@NotNull String whoLeave) {
        JsonObject result = new JsonObject();
        result.addProperty("status", "leave");
        result.addProperty("name", whoLeave);

        //noinspection ConstantConditions
        return result.toString();
    }

    @NotNull
    public String createMessageStartGame(@NotNull GameSession gameSession,
                                         @NotNull String myName,
                                         int gameTime) {
        JsonObject result = new JsonObject();
        result.addProperty("status", "start");
        result.addProperty("your_name", myName);
        result.addProperty("time_of_game", gameTime / 1000);

        JsonArray arr = new JsonArray();
        for (GameUser player: gameSession.getGameUsers()) {
            JsonObject guser = new JsonObject();
            guser.addProperty("name", player.getName());
            arr.add(guser);
        }

        result.add("players", arr);

        //noinspection ConstantConditions
        return result.toString();
    }

    @NotNull
    public String createMessageIncrementScore(@NotNull GameSession gameSession) {
        JsonObject result = new JsonObject();
        result.addProperty("status", "scores");

        JsonArray arr = new JsonArray();
        for (GameUser player: gameSession.getGameUsers()) {
            JsonObject guser = new JsonObject();
            guser.addProperty("name", player.getName());
            guser.addProperty("score", player.getScore());
            arr.add(guser);
        }
        result.add("players", arr);

        //noinspection ConstantConditions
        return result.toString();
    }

    @NotNull
    public String createMessageGameOver(@NotNull String nameWinner) {
        JsonObject result = new JsonObject();
        result.addProperty("status", "finish");
        result.addProperty("win", nameWinner);

        //noinspection ConstantConditions
        return result.toString();
    }

    @NotNull
    public String createMessageTextInChat(@NotNull String authorName, @NotNull String text) {
        JsonObject result = new JsonObject();
        result.addProperty("status", "message");
        result.addProperty("name", authorName);
        result.addProperty("text", text);

        //noinspection ConstantConditions
        return result.toString();
    }
}
