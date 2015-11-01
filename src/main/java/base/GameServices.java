package base;

import game.GameMechanics;
import game.WebSocketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import resourceSystem.Resource;
import resourceSystem.ResourceFactory;
import utils.VFS;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ilya on 27.10.15.
 */
public class GameServices {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger LOGGER = LogManager.getLogger(GameServices.class);

    @NotNull
    private AccountService accountService;
    @NotNull
    private WebSocketService webSocketService;
    @NotNull
    private GameMechanics gameMechanics;

    public GameServices(@NotNull String resourcesDirectory) {
        this.accountService = new AccountService();
        this.webSocketService = new WebSocketService();
        Map<String, Resource> resources = loadResources(resourcesDirectory);
        this.gameMechanics = new GameMechanics(webSocketService, resources);
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

    @NotNull
    private Map<String, Resource> loadResources(@NotNull String resourcesDirectory) {
        Map<String, Resource> resources = new HashMap<>();
        VFS vfs = new VFS();
        Iterator<String> iter = vfs.getIterator(resourcesDirectory);
        while (iter.hasNext()) {
            String fileName = iter.next();
            System.out.println(fileName);
            if (fileName != null && fileName.contains(".xml")) {
                Resource resource = (Resource) ResourceFactory.instance().get(fileName);
                if (resource != null && resource.getClass() != null) {
                    resources.put(resource.getClass().getSimpleName(), resource);
                } else {
                    LOGGER.error("Fail to create resource class");
                }
            }
        }
        return resources;
    }
}
