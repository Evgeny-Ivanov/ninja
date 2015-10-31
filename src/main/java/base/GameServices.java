package base;

import game.GameMechanics;
import game.WebSocketService;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ilya on 27.10.15.
 */
public class GameServices {
    @NotNull
    private AccountService accountService;
    @NotNull
    private WebSocketService webSocketService;
    @NotNull
    private GameMechanics gameMechanics;

    public GameServices(@NotNull AccountService accountService) {
        this.accountService = accountService;
        this.webSocketService = new WebSocketService();
        this.gameMechanics = new GameMechanics(webSocketService);
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
    public GameMechanics getGameMechanics() {
        return gameMechanics;
    }
}
