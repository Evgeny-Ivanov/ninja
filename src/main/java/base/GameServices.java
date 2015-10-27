package base;

import game.GameMechanics;
import game.GameMechanicsImpl;
import game.WebSocketService;
import game.WebSocketServiceImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ilya on 27.10.15.
 */
public class GameServices {
    @NotNull
    AccountService accountService;
    @NotNull
    WebSocketService webSocketService;
    @NotNull
    GameMechanics gameMechanics;

    public GameServices(@NotNull AccountService accountService) {
        this.accountService = accountService;
        this.webSocketService = new WebSocketServiceImpl();
        this.gameMechanics = new GameMechanicsImpl(webSocketService);
    }

    @NotNull
    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(@NotNull AccountService accountService) {
        this.accountService = accountService;
    }

    @NotNull
    public WebSocketService getWebSocketService() {
        return webSocketService;
    }

    @NotNull
    public void setWebSocketService(@NotNull WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @NotNull
    public GameMechanics getGameMechanics() {
        return gameMechanics;
    }

    public void setGameMechanics(@NotNull GameMechanics gameMechanics) {
        this.gameMechanics = gameMechanics;
    }
}
