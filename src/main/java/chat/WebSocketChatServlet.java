package chat;

import base.UrlParameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.jetbrains.annotations.NotNull;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ilya on 24.10.15.
 */
public class WebSocketChatServlet extends WebSocketServlet {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger logger = LogManager.getLogger(WebSocketChatServlet.class);
    private static final int LOGOUT_TIME = 60 * 1000;
    @NotNull
    private UrlParameters chatUrlParameters;

    public WebSocketChatServlet(@NotNull UrlParameters chatUrlParameters) {
        this.chatUrlParameters = chatUrlParameters;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(new ChatWebSocketCreator());
        logger.info("call WebSocketChatServlet.configure");
    }

    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        HttpSession hs = request.getSession();
        if (hs == null || hs.getId() == null) {
            return;
        }

        if (hs.getAttribute("name") == null || hs.getAttribute("name").equals("Incognitto")) {
            return;
        }

        Map<String, Object> pageVariables = new HashMap<>();

        pageVariables.put("name", hs.getAttribute("name"));
        pageVariables.put("host", chatUrlParameters.getHost());
        pageVariables.put("port", chatUrlParameters.getPort());
        pageVariables.put("socket_url", chatUrlParameters.getSocketUrl());

        try (PrintWriter pw = response.getWriter()) {
            if (pw != null) {
                pw.println(PageGenerator.getPage("chat.html", pageVariables));
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}