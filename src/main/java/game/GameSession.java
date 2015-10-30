package game;

import java.util.*;

/**
 * Created by ilya on 27.10.15.
 */
public class GameSession {
    private final long startTime;
    private List<GameUser> playersGameUsers;
    private Map<String, GameUser> users = new HashMap<>();

    public GameSession(List<String> namesPlayers) {
        startTime = new Date().getTime();

        for (String nameUser: namesPlayers) {
            List<String> arrayEnemyNames = new ArrayList<String>(namesPlayers);
            arrayEnemyNames.remove(nameUser);
            GameUser gameUser = new GameUser(nameUser, arrayEnemyNames);
            users.put(nameUser, gameUser);
            playersGameUsers.add(gameUser);
        }
    }

    public GameUser getSelf(String nameUser) {
        return users.get(nameUser);
    }

    public long getSessionTime(){
        return new Date().getTime() - startTime;
    }

    public  String getNameWinner(){
        String nameWinner = "";
        int maxScore = 0;
        for (GameUser gameUser: playersGameUsers) {
            if (gameUser.getMyScore() > maxScore) {
                maxScore = gameUser.getMyScore();
                nameWinner = gameUser.getMyName();
            }
        }
        return nameWinner;
    }

    public List<GameUser> getGameUsers() {
        return playersGameUsers;
    }


    public List<GameUser> getEnemyUsers(String user) {
        List<GameUser> arrayEnemyGameUsers = new ArrayList<>(playersGameUsers);
        GameUser enemyGameUser = users.get(user);
        arrayEnemyGameUsers.remove(enemyGameUser);

        return arrayEnemyGameUsers;
    }

}
