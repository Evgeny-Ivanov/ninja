package game;

import base.GameContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import resourceSystem.GMResource;
import resourceSystem.ResourcesContext;
import utils.TimeHelper;

import java.util.*;

/**
 * Created by ilya on 27.10.15.
 */
public class GameMechanics {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger LOGGER = LogManager.getLogger(GameMechanics.class);

    @NotNull
    private GMResource gMResource;
    @NotNull
    private WebSocketService webSocketService;

    @NotNull
    private Map<String, GameSession> nameToGame = new HashMap<>();
    @NotNull
    private Set<GameSession> allSessions = new HashSet<>();
    @NotNull
    private List<String> namesPlayers = new ArrayList<>();


    public GameMechanics() {
        WebSocketService newWebSocketService = (WebSocketService)GameContext.getInstance().get(WebSocketService.class);
        if (newWebSocketService == null) {
            LOGGER.error("newWebSocketService == null");
            throw new NullPointerException();
        }
        this.webSocketService = newWebSocketService;

        ResourcesContext resourcesContext = (ResourcesContext)GameContext.getInstance().get(ResourcesContext.class);
        if (resourcesContext == null) {
            LOGGER.error("resourcesContext == null");
            throw new NullPointerException();
        }
        this.webSocketService = newWebSocketService;

        GMResource newGMResource = (GMResource) (resourcesContext.get(GMResource.class));
        if (newGMResource == null) {
            LOGGER.error("newGMResource == null");
            throw new NullPointerException();
        }
        this.gMResource = newGMResource;
    }

    public void addUser(@NotNull String userName) {
        namesPlayers.add(userName);

        if (namesPlayers.size() == gMResource.getNumberPlayers()) {
            startGame();
            namesPlayers.clear();
        }
    }

    public boolean removeUser(@NotNull String userName) {
        if (namesPlayers.remove(userName)) {
            return false;
        }

        GameSession gameSession = nameToGame.get(userName);
        if (gameSession == null) {
            return false;
        }

        List<GameUser> gameUsers = gameSession.getGameUsers();
        //noinspection Convert2streamapi
        for (GameUser user: gameUsers) {
            if (!userName.equals(user.getName())) {
                user.getPlayersGameUsers().remove(nameToGame.get(userName).getGameUser(userName));
                webSocketService.notifyAboutLeave(user.getName(), userName);
            }
        }

        gameSession.removeGameUser(userName);
        nameToGame.remove(userName);

        if (gameSession.getGameUsers().isEmpty()) {
            allSessions.remove(gameSession);
        }
        return true;
    }

    public void incrementScore(@NotNull String userName) {
        GameSession gameSession = nameToGame.get(userName);
        if (gameSession == null) {
            LOGGER.error("userGameSession == null");
            return;
        }

        GameUser gameUser = gameSession.getGameUser(userName);

        if (gameUser  == null) {
            LOGGER.error("gameUser == null");
            return;
        }

        gameUser.incrementScore();

        //noinspection Convert2streamapi
        for (GameUser user: gameSession.getGameUsers()) {
            webSocketService.notifyAboutScores(user.getName(), gameSession);
        }
    }

    public void messageInChat(@NotNull String userName, @NotNull String message) {
        GameSession gameSession = nameToGame.get(userName);
        if (gameSession == null) {
            LOGGER.error("userGameSession == null");
            return;
        }

        for (GameUser user: gameSession.getGameUsers())  {
            webSocketService.notifyAboutMessage(user.getName(), userName, message);
        }
    }

    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            gmStep();
            TimeHelper.sleep(gMResource.getStepTime());
        }
    }

    private void gmStep() {
        for (GameSession session : allSessions) {
            if (session.getSessionTime() > gMResource.getGameTime()) {
                finishGame(session);
            }
        }
    }

    private void finishGame(@NotNull GameSession session) {
        String nameWinner = session.getNameWinner();

        for (GameUser user: session.getGameUsers())  {
            webSocketService.notifyGameOver(user, nameWinner);
            nameToGame.remove(user.getName());
        }

        allSessions.remove(session);
    }

    private void startGame() {
        GameSession gameSession = new GameSession(namesPlayers);
        allSessions.add(gameSession);

        for (String userName: namesPlayers) {
            nameToGame.put(userName, gameSession);
            GameUser gameUser = gameSession.getGameUser(userName);

            if (gameUser != null) {
                webSocketService.notifyStartGame(userName, gameSession, gMResource.getGameTime());
            } else {
                LOGGER.error("gameuser == null");
            }
        }
    }
}