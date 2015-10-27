package game;

import base.AccountService;
import base.GameServices;
import base.UrlParameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.jetbrains.annotations.NotNull;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ilya on 27.10.15.
 */
public class WebSocketGameServlet extends WebSocketServlet {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger logger = LogManager.getLogger(WebSocketGameServlet.class);
    private static final int IDLE_TIME = 60 * 1000;
    @NotNull
    private GameServices gameServices;
    @NotNull
    private UrlParameters gameUrlParameters;
    @NotNull
    private UrlParameters chatUrlParameters;

    public WebSocketGameServlet(@NotNull GameServices gameServices,
                                @NotNull UrlParameters gameUrlParameters,
                                @NotNull UrlParameters chatUrlParameters) {
        this.gameServices = gameServices;
        this.gameUrlParameters = gameUrlParameters;
        this.chatUrlParameters = chatUrlParameters;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(IDLE_TIME);
        factory.setCreator(new GameWebSocketCreator(gameServices));
        logger.info("call WebSocketGameServlet.configure");

    }

    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        HttpSession hs = request.getSession();
        if (hs == null || hs.getId() == null) {
            return;
        }
        if (hs.getAttribute("name") == null || hs.getAttribute("name").equals("Incognitto")) {
            return;
        }

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("name", hs.getAttribute("name"));
        pageVariables.put("host_game", gameUrlParameters.getHost());
        pageVariables.put("port_game", gameUrlParameters.getPort());
        pageVariables.put("socket_url_game", gameUrlParameters.getSocketUrl());
        pageVariables.put("host_chat", chatUrlParameters.getHost());
        pageVariables.put("port_chat", chatUrlParameters.getPort());
        pageVariables.put("socket_url_chat", chatUrlParameters.getSocketUrl());

        try (PrintWriter pw = response.getWriter()) {
            if (pw != null) {
                pw.println(PageGenerator.getPage("game.html", pageVariables));
            }
        }


        response.setStatus(HttpServletResponse.SC_OK);
    }
}