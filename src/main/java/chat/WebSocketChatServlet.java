package chat;

import main.AccountService;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.jetbrains.annotations.NotNull;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ilya on 24.10.15.
 */
@WebServlet(name = "WebSocketChatServlet", urlPatterns = {"/chat"})
public class WebSocketChatServlet extends WebSocketServlet {

    private final static int LOGOUT_TIME = 10 * 60 * 1000;
    private String host;
    private String port;

    public WebSocketChatServlet(String host, String port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(new ChatWebSocketCreator());
        System.out.println("WebSocketChatServlet: configure");
    }

    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        Map<String, Object> pageVariables = new HashMap<>();


        HttpSession hs = request.getSession();
        if (hs == null || hs.getId() == null) {
            return;
        }

        if (hs.getAttribute("name") == null /* || hs.getAttribute("name").equals("Incognitto") */) {
            return;
        }

        try (PrintWriter pw = response.getWriter()) {
            if (pw != null) {
                pageVariables.put("name", hs.getAttribute("name"));
                pageVariables.put("host", host);
                pageVariables.put("port", port);
                response.setContentType("text/html");
                pw.println(PageGenerator.getPage("chat.html", pageVariables));
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}