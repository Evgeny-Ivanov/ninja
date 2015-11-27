package game;

import base.GameContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.jetbrains.annotations.NotNull;
import base.Configuration;
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
    static final Logger LOGGER = LogManager.getLogger(WebSocketGameServlet.class);
    private static final int IDLE_TIME = 10 * 60 * 1000;



    @Override
    public void configure(WebSocketServletFactory factory) {
        //noinspection ConstantConditions
        factory.getPolicy().setIdleTimeout(IDLE_TIME);
        factory.setCreator(new GameWebSocketCreator());
        LOGGER.info("call configure");
    }

    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        HttpSession hs = request.getSession();
        if (hs == null || hs.getId() == null) {
            return;
        }
        if (hs.getAttribute("name") == null || "Incognitto".equals(hs.getAttribute("name"))) {
            return;
        }

        Configuration conf = (Configuration)GameContext.getInstance().get(Configuration.class);

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("name", hs.getAttribute("name"));
        pageVariables.put("host_game", conf.getGameSocketHost());
        pageVariables.put("port_game", conf.getGameSocketPort());
        pageVariables.put("url_game", conf.getGameSocketUrl());

        try (PrintWriter pw = response.getWriter()) {
            if (pw != null) {
                pw.println(PageGenerator.getPage("game.html", pageVariables));
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}