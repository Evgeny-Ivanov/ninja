package game;

import utils.TimeHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by ilya on 27.10.15.
 */
public class GameMechanicsImpl implements GameMechanics {
    private WebSocketService webSocketService;
    private static final int STEP_TIME = 100;
    private static final int WAIT_TIME = 3 * 1000;
    private static final int gameTime = 60 * 1000;
    private Map<String, GameSession> nameToGame = new HashMap<>();
    private Set<GameSession> allSessions = new HashSet<>();
    private String[] namesPlayers = new String[2];

    public GameMechanicsImpl(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    public void addUser(String user) {
        if (namesPlayers[0] != null && namesPlayers[1] == null) {
            namesPlayers[1] = user;
            startGame(namesPlayers);
        } else if (namesPlayers[0] == null && namesPlayers[1] == null) {
            namesPlayers[0] = user;
        } else {
            TimeHelper.sleep(WAIT_TIME);
            this.addUser(user);
        }
    }

    public void incrementScore(String userName) {
        GameSession myGameSession = nameToGame.get(userName);
        GameUser myUser = myGameSession.getSelf(userName);
        myUser.incrementMyScore();
        GameUser enemyUser = myGameSession.getEnemy(userName);
        enemyUser.incrementEnemyScore();
        webSocketService.notifyMyNewScore(myUser);
        webSocketService.notifyEnemyNewScore(enemyUser);
    }

    @Override
    public void run() {
        while (true) {
            gmStep();
            TimeHelper.sleep(STEP_TIME);
        }
    }

    private void gmStep() {
        for (GameSession session : allSessions) {
            if (session.getSessionTime() > gameTime) {
                finishGame(session);
            }
        }
    }

    private void finishGame(GameSession session) {
        boolean firstWin = session.isFirstWin();
        webSocketService.notifyGameOver(session.getFirst(), firstWin);
        webSocketService.notifyGameOver(session.getSecond(), !firstWin);


        allSessions.remove(session);
        nameToGame.remove(namesPlayers[0]);
        nameToGame.remove(namesPlayers[1]);
        namesPlayers[0] = null;
        namesPlayers[1] = null;

    }

    private void startGame(String[] namesPlayers) {
        GameSession gameSession = new GameSession(namesPlayers[0], namesPlayers[1]);
        allSessions.add(gameSession);
        nameToGame.put(namesPlayers[0], gameSession);
        nameToGame.put(namesPlayers[1], gameSession);

        webSocketService.notifyStartGame(gameSession.getSelf(namesPlayers[0]));
        webSocketService.notifyStartGame(gameSession.getSelf(namesPlayers[1]));
    }
}