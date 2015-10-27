package main;


import admin.AdminPageServlet;
import chat.WebSocketChatServlet;
import frontend.LogoutServlet;
import frontend.MainPageServlet;
import frontend.SignInServlet;
import frontend.SignUpServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.jetbrains.annotations.NotNull;

import javax.servlet.Servlet;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author v.chibrikov
 */
public class Main {
    static final Logger logger = LogManager.getLogger(WebSocketChatServlet.class);

    public static final String ADMIN_PAGE_URL = "/admin";
    public static final String SIGNIN_PAGE_URL = "/api/v1/auth/signin";
    public static final String SIGNUP_PAGE_URL = "/api/v1/auth/signup";
    public static final String LOGOUT_PAGE_URL = "/api/v1/auth/logout";
    public static final String MAINPAGE_PAGE_URL = "/mainpage";
    public static final String CHAT_SOCKET_URL = "/chat";

    public static final String PROPERTIES_FILE = "cfg/server.properties";

    public static void main(@NotNull String[] args) {
        String port;
        String host;

        try (final FileInputStream fis = new FileInputStream(PROPERTIES_FILE)) {
            final Properties properties = new Properties();
            properties.load(fis);

            host = properties.getProperty("host");
            port = properties.getProperty("port");
        } catch (IOException e) {
            logger.error(e);
            return;
        }

        if (port == null || host == null) {
            logger.error("Port or host is null");
            return;
        }

        logger.info("Host: {} Port: {}", host, port);

        Server server = new Server(new Integer(port));
        AccountService accountService = new AccountService();

        Servlet signIn = new SignInServlet(accountService);
        Servlet signUp = new SignUpServlet(accountService);
        Servlet logout = new LogoutServlet(accountService);
        Servlet admin = new AdminPageServlet(accountService, server);
        Servlet mainPage = new MainPageServlet();
        WebSocketServlet chat1 = new WebSocketChatServlet(host, port, CHAT_SOCKET_URL);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setVirtualHosts(new String[]{host});

        context.addServlet(new ServletHolder(signIn), SIGNIN_PAGE_URL);
        context.addServlet(new ServletHolder(signUp), SIGNUP_PAGE_URL);
        context.addServlet(new ServletHolder(admin), ADMIN_PAGE_URL);
        context.addServlet(new ServletHolder(logout), LOGOUT_PAGE_URL);
        context.addServlet(new ServletHolder(mainPage), MAINPAGE_PAGE_URL);
        context.addServlet(new ServletHolder(chat1), CHAT_SOCKET_URL);

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("static");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        server.setHandler(handlers);

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
