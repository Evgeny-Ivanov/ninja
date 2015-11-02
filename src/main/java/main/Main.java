package main;


import admin.AdminPageServlet;
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
import utils.Configuration;
import javax.servlet.Servlet;

/**
 * @author v.chibrikov
 */
public class Main {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(@NotNull String[] args) {
        Configuration configuration = Configuration.getInstance();

        int port = configuration.getPort();
        String host = configuration.getHost();

        if(host == null){
            host = "localhost";
        }

        LOGGER.info("Host: {} Port: {}", host, port);

        UrlParameters gameplaySocketUrl = new UrlParameters(host,Integer.toString(port),configuration.getGameSocketUrl());
        GameServices gameServices = new GameServices(configuration.getResourcesDirectory());

        gameServices.getAccountService().autoFullUsers();

        Server server = new Server(port);

        Servlet mainPage = new MainPageServlet();
        Servlet signIn = new SignInServlet(gameServices.getAccountService());
        Servlet signUp = new SignUpServlet(gameServices.getAccountService());
        Servlet logout = new LogoutServlet(gameServices.getAccountService());
        Servlet admin = new AdminPageServlet(gameServices.getAccountService(), server);
        WebSocketServlet game = new WebSocketGameServlet(gameServices, gameplaySocketUrl);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        //context.setVirtualHosts(new String[]{host});

        context.addServlet(new ServletHolder(signIn), configuration.getSigninPageUrl());
        context.addServlet(new ServletHolder(signUp), configuration.getSignupPageUrl());
        context.addServlet(new ServletHolder(admin), configuration.getAdminPageUrl());
        context.addServlet(new ServletHolder(logout), configuration.getLogoutPageUrl());
        context.addServlet(new ServletHolder(mainPage), configuration.getMainPageUrl());
        context.addServlet(new ServletHolder(game), configuration.getGameSocketUrl());

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
