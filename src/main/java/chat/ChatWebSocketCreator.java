package chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ilya on 24.10.15.
 */
public class ChatWebSocketCreator implements WebSocketCreator {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger logger = LogManager.getLogger(WebSocketChatServlet.class);
    private Set<ChatWebSocket> users;

    public ChatWebSocketCreator() {
        //this.users = Collections.newSetFromMap(new ConcurrentHashMap<ChatWebSocket, Boolean>());
        this.users = new HashSet<ChatWebSocket>();
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        ChatWebSocket socket = new ChatWebSocket(users);
        logger.info("createWebSocket");
        return socket;
    }
}