package game;

import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * Created by ilya on 27.10.15.
 */
public class GameUser {
    @NotNull
    private final String name;
    private int score;
    @NotNull
    private List<GameUser> playersGameUsers;

    public GameUser(@NotNull String name, @NotNull List<GameUser> playersGameUsers) {
        this.name = name;
        this.score = 0;
        this.playersGameUsers = playersGameUsers;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @NotNull
    public List<GameUser> getPlayersGameUsers() {
        return playersGameUsers;
    }

    public void incrementScore() {
        score++;
    }

}
