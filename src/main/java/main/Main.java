package main;

import admin.AdminPageServlet;
import base.AccountService;
import base.GameContext;
import database.DBAccountService;
import frontend.LogoutServlet;
import frontend.MainPageServlet;
import frontend.SignInServlet;
import frontend.SignUpServlet;
import game.GameMechanics;
import game.WebSocketGameServlet;
import game.WebSocketService;
import game.WebSocketServiceImpl;
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
import base.Configuration;
import resourcesystem.ResourcesContext;

import javax.servlet.Servlet;

/**
 * @author v.chibrikov
 */
public class Main {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger LOGGER = LogManager.getLogger(Main.class);

    @NotNull
    private static final String PROPERTIES_FILE = "cfg/server.properties";
    @NotNull
    private static final String PROPERTIES_FILE_DB = "cfg/db.properties";

    public static void main(@NotNull String[] args) {
        GameContext gameСontext = GameContext.getInstance();

        Configuration conf = new Configuration(PROPERTIES_FILE);
        gameСontext.add(Configuration.class, conf);

        AccountService accountService = new DBAccountService(PROPERTIES_FILE_DB);
        ((DBAccountService)accountService).openConnection();
        gameСontext.add(AccountService.class, accountService);

        WebSocketService webSocketService = new WebSocketServiceImpl();
        gameСontext.add(WebSocketService.class, webSocketService);

        ResourcesContext resourcesContext = new ResourcesContext(conf.getResourcesDirectory());
        gameСontext.add(ResourcesContext.class, resourcesContext);

        GameMechanics gameMechanics = new GameMechanics();
        gameСontext.add(GameMechanics.class, gameMechanics);

        Server server = new Server(conf.getPort());

        Servlet mainPage = new MainPageServlet();
        Servlet signIn = new SignInServlet(accountService);
        Servlet signUp = new SignUpServlet(accountService);
        Servlet logout = new LogoutServlet(accountService);
        Servlet admin = new AdminPageServlet(accountService, server);
        WebSocketServlet game = new WebSocketGameServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        //context.setVirtualHosts(new String[]{configuration.getHost()});

        context.addServlet(new ServletHolder(signIn), conf.getSigninPageUrl());
        context.addServlet(new ServletHolder(signUp), conf.getSignupPageUrl());
        context.addServlet(new ServletHolder(admin), conf.getAdminPageUrl());
        context.addServlet(new ServletHolder(logout), conf.getLogoutPageUrl());
        context.addServlet(new ServletHolder(mainPage), conf.getMainPageUrl());
        context.addServlet(new ServletHolder(game), conf.getGameSocketUrl());

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

        gameMechanics.run();

        ((DBAccountService)accountService).closeConnection();
    }
}
