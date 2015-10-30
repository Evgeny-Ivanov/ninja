package game;

import utils.TimeHelper;

import java.util.*;

/**
 * Created by ilya on 27.10.15.
 */
public class GameMechanicsImpl implements GameMechanics {
    private WebSocketService webSocketService;
    private static final int STEP_TIME = 100;
    private static final int gameTime = 60 * 1000;
    private int numberPlayers = 3;
    private Map<String, GameSession> nameToGame = new HashMap<>();
    private Set<GameSession> allSessions = new HashSet<>();
    private List<String> namesPlayers = new ArrayList<>();

    public GameMechanicsImpl(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    public void addUser(String userName) {
        if (namesPlayers.size() < numberPlayers) {
            namesPlayers.add(userName);
        } else {
            startGame(namesPlayers);
            namesPlayers.clear();
        }
    }

    public void incrementScore(String userName) {
        GameSession myGameSession = nameToGame.get(userName);
        GameUser myUser = myGameSession.getSelf(userName);
        myUser.incrementMyScore();
        List<GameUser> enemyUsers = myGameSession.getEnemyUsers(userName);
        for (GameUser enemyUser: enemyUsers) {
            enemyUser.incrementEnemyScore("userName");
        }

        webSocketService.notifyAboutScore(myUser);
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
        String nameWinner = session.getNameWinner();

        for (GameUser user: session.getGameUsers())  {
            webSocketService.notifyGameOver(user, nameWinner);
            nameToGame.remove(user.getMyName());
        }

        allSessions.remove(session);
    }

    private void startGame(List<String> namesPlayers) {
        GameSession gameSession = new GameSession(namesPlayers);
        allSessions.add(gameSession);

        for (String userName: namesPlayers) {
            nameToGame.put(userName, gameSession);
            webSocketService.notifyStartGame(gameSession.getSelf(userName));
        }
    }
}