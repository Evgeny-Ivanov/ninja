package game;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Created by ilya on 27.10.15.
 */
public class GameSession {
    private final long startTime;
    @NotNull
    private List<GameUser> playersGameUsers = new ArrayList<>();
    @NotNull
    private Map<String, GameUser> users = new HashMap<>();

    public GameSession(@NotNull List<String> namesPlayers) {
        startTime = new Date().getTime();

        for (String nameUser: namesPlayers) {
            GameUser gameUser = new GameUser(nameUser, playersGameUsers);
            users.put(nameUser, gameUser);
            playersGameUsers.add(gameUser);
        }
    }

    public long getSessionTime(){
        return new Date().getTime() - startTime;
    }

    @Nullable
    public GameUser getGameUser(@NotNull String nameUser) {
        return users.get(nameUser);
    }

    @NotNull
    public List<GameUser> getGameUsers() {
        return playersGameUsers;
    }

    @NotNull
    public String getNameWinner(){
        String nameWinner = "";
        int maxScore = 0;
        for (GameUser gameUser: playersGameUsers) {
            if (gameUser.getScore() >= maxScore) {
                maxScore = gameUser.getScore();
                nameWinner = gameUser.getName();
            }
        }
        return nameWinner;
    }

}
