package game;

import base.GameContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import resourcesystem.GMResource;
import resourcesystem.ResourcesContext;
import utils.TimeHelper;
import java.util.*;

/**
 * Created by ilya on 27.10.15.
 */
public class GameMechanics {
    @NotNull @SuppressWarnings("ConstantConditions")
    static final Logger LOGGER = LogManager.getLogger(GameMechanics.class);

    @NotNull
    private GMResource gMResource;
    @NotNull
    private WebSocketService webSocketService;

    @NotNull
    private final Map<String, GameSession> nameToGame = new HashMap<>();
    @NotNull
    private final Set<GameSession> allSessions = new HashSet<>();
    @NotNull
    private final Set<String> namesPlayers = new HashSet<>();
    @NotNull
    private final GameMessager gameMessager = new GameMessager(this);


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

    @NotNull
    public GameMessager getGameMessager() {
        return gameMessager;
    }

    public void run() {
        int stepTime = gMResource.getStepTime();
        int gameTime = gMResource.getGameTime();
        //noinspection InfiniteLoopStatement
        while (true) {
            gmStep(gameTime);
            TimeHelper.sleep(stepTime);
        }
    }

    private void gmStep(int gameTime) {
        for (Iterator<GameSession> iterator = allSessions.iterator(); iterator.hasNext();) {
            GameSession session = iterator.next();
            if (session == null) {
                LOGGER.warn("session == null");
            } else if (session.getSessionTime() > gameTime) {
                finishGame(session);
                iterator.remove();
            }
        }
    }

    public void addUser(@NotNull String userName) {
        namesPlayers.add(userName);

        if (namesPlayers.size() == gMResource.getNumberPlayers()) {
            startGame();
            namesPlayers.clear();
        }
    }

    private void startGame() {
        GameSession gameSession = new GameSession(namesPlayers);
        allSessions.add(gameSession);
        LOGGER.info("start game");

        for (String userName: namesPlayers) {
            nameToGame.put(userName, gameSession);
            GameUser gameUser = gameSession.getGameUser(userName);

            String message = gameMessager.createMessageStartGame(gameSession, userName, gMResource.getGameTime());

            if (gameUser != null) {
                webSocketService.notify(userName, message);
            } else {
                LOGGER.error("gameuser == null");
            }
        }
    }

    public boolean removeUser(@NotNull String userName) {
        if (namesPlayers.remove(userName)) {
            return true;
        }

        GameSession gameSession = nameToGame.get(userName);
        if (gameSession == null) {
            return false;
        }

        gameSession.removeGameUser(userName);
        nameToGame.remove(userName);

        if (gameSession.getGameUsers().isEmpty()) {
            allSessions.remove(gameSession);
            return true;
        }

        String message = gameMessager.createMessageLeave(userName);

        for (GameUser user: gameSession.getGameUsers()) {
            webSocketService.notify(user.getName(), message);
        }

        return true;
    }

    private void finishGame(@NotNull GameSession session) {
        String nameWinner = session.getNameWinner();
        String message = gameMessager.createMessageGameOver(nameWinner);
        LOGGER.info("finish game");

        for (GameUser user: session.getGameUsers())  {
            webSocketService.notify(user.getName(), message);
            nameToGame.remove(user.getName());
        }
    }

    public void incrementScore(@NotNull String userName) {
        GameSession gameSession = nameToGame.get(userName);
        if (gameSession == null) {
            LOGGER.warn("userGameSession == null");
            return;
        }

        GameUser gameUser = gameSession.getGameUser(userName);
        if (gameUser  == null) {
            LOGGER.warn("gameUser == null");
            return;
        }

        gameUser.incrementScore();

        String message = gameMessager.createMessageIncrementScore(gameSession);
        for (GameUser user: gameSession.getGameUsers()) {
            webSocketService.notify(user.getName(), message);
        }
    }

    public void textInChat(@NotNull String authorName, @NotNull String text) {
        GameSession gameSession = nameToGame.get(authorName);
        if (gameSession == null) {
            LOGGER.warn("userGameSession == null");
            return;
        }

        String message = gameMessager.createMessageTextInChat(authorName, text);
        for (GameUser user: gameSession.getGameUsers())  {
            webSocketService.notify(user.getName(), message);
        }
    }
}