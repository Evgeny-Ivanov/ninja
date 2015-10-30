package game;

import java.util.*;

/**
 * Created by ilya on 27.10.15.
 */
public class GameSession {
    private final long startTime;
    private List<GameUser> playersGameUsers = new ArrayList<>();
    private Map<String, GameUser> users = new HashMap<>();

    public GameSession(List<String> namesPlayers) {
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

    public GameUser getGameUser(String nameUser) {
        return users.get(nameUser);
    }

    public List<GameUser> getGameUsers() {
        return playersGameUsers;
    }

    public  String getNameWinner(){
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
