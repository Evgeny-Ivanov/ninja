package game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ilya on 27.10.15.
 */
public class GameUser {
    private final String myName;
    private int myScore = 0;
    private Map<String, Integer> enemyNamesToScore;

    public GameUser(String myName, List<String> arrayEnemyNames) {
        this.myName = myName;
        enemyNamesToScore = new HashMap<>();
        for (String nameEnemy: arrayEnemyNames) {
            this.enemyNamesToScore.put(nameEnemy, 0);
        }
    }

    public String getMyName() {
        return myName;
    }

    public int getMyScore() {
        return myScore;
    }

    public Map<String, Integer> getEnemyNameAndScore() {
        return enemyNamesToScore;
    }

    public void incrementMyScore() {
        myScore++;
    }

    public void incrementEnemyScore(String nameUser) {
        enemyNamesToScore.put(nameUser, enemyNamesToScore.put(nameUser));
    }


}
