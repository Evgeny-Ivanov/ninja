package main;


import admin.AdminPageServlet;
import base.AccountService;
import base.GameServices;
import base.UrlParameters;
import frontend.LogoutServlet;
import frontend.MainPageServlet;
import frontend.SignInServlet;
import frontend.SignUpServlet;
import game.WebSocketGameServlet;
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
import resourceSystem.Resource;
import resourceSystem.ResourceFactory;
import utils.VFS;

import javax.servlet.Servlet;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @author v.chibrikov
 */
public class Main {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static final String SIGNIN_PAGE_URL = "/api/v1/auth/signin";
    public static final String SIGNUP_PAGE_URL = "/api/v1/auth/signup";
    public static final String LOGOUT_PAGE_URL = "/api/v1/auth/logout";
    public static final String MAINPAGE_PAGE_URL = "/mainpage";
    public static final String ADMIN_PAGE_URL = "/admin";
    public static final String GAME_SOCKET_URL = "/gameplay";
    public static final String PROPERTIES_FILE = "cfg/server.properties";
    public static final String RESOURCES_DIRECTORY = "./data";

    public static void main(@NotNull String[] args) {
        String port;
        String host;

        try (final FileInputStream fis = new FileInputStream(PROPERTIES_FILE)) {
            final Properties properties = new Properties();
            properties.load(fis);

            host = properties.getProperty("host");
            port = properties.getProperty("port");
        } catch (IOException e) {
            LOGGER.error(e);
            return;
        }

        if (port == null || host == null) {
            LOGGER.error("Port or host is null");
            return;
        }

        LOGGER.info("Host: {} Port: {}", host, port);

        UrlParameters gameUrlParameters = new UrlParameters(host, port, GAME_SOCKET_URL);
        GameServices gameServices = new GameServices(RESOURCES_DIRECTORY);
        gameServices.getAccountService().autoFullUsers();
        Server server = new Server(new Integer(port));


        Servlet signIn = new SignInServlet(gameServices.getAccountService());
        Servlet signUp = new SignUpServlet(gameServices.getAccountService());
        Servlet logout = new LogoutServlet(gameServices.getAccountService());
        Servlet admin = new AdminPageServlet(gameServices.getAccountService(), server);
        Servlet mainPage = new MainPageServlet();
        WebSocketServlet game = new WebSocketGameServlet(gameServices, gameUrlParameters);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setVirtualHosts(new String[]{host});

        context.addServlet(new ServletHolder(signIn), SIGNIN_PAGE_URL);
        context.addServlet(new ServletHolder(signUp), SIGNUP_PAGE_URL);
        context.addServlet(new ServletHolder(admin), ADMIN_PAGE_URL);
        context.addServlet(new ServletHolder(logout), LOGOUT_PAGE_URL);
        context.addServlet(new ServletHolder(mainPage), MAINPAGE_PAGE_URL);
        context.addServlet(new ServletHolder(game), GAME_SOCKET_URL);

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("static");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        server.setHandler(handlers);

        try {
            server.start();
        } catch (Exception e) {
            LOGGER.error("Server isn't started");
            e.printStackTrace();
        }

        gameServices.getGameMechanics().run();
    }
}
