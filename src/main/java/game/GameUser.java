package game;

import java.util.List;

/**
 * Created by ilya on 27.10.15.
 */
public class GameUser {
    private final String name;
    private int score = 0;
    private List<GameUser> playersGameUsers;

    public GameUser(String name, List<GameUser> playersGameUsers) {
        this.name = name;
        this.score = 0;
        this.playersGameUsers = playersGameUsers;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public List<GameUser> getPlayersGameUsers() {
        return playersGameUsers;
    }

    public void incrementScore() {
        score++;
    }

}
