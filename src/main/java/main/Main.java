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
import utils.Configuration;

import javax.servlet.Servlet;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author v.chibrikov
 */
public class Main {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(@NotNull String[] args) {
        Configuration configuration;
        try {
            configuration = Configuration.getInstance();
        }catch (NullPointerException e){
            e.printStackTrace();
            return;
        }

        int port = configuration.getPort();
        String host = configuration.getHost();

        LOGGER.info("Host: {} Port: {}", host, port);

        Server server = new Server(port);

        UrlParameters gameUrlParameters = new UrlParameters(host,Integer.toString(port),configuration.getGameSocketUrl());
        AccountService accountService = new AccountService();
        accountService.autoFullUsers();
        GameServices gameServices = new GameServices(accountService);

        Servlet signIn = new SignInServlet(accountService);
        Servlet signUp = new SignUpServlet(accountService);
        Servlet logout = new LogoutServlet(accountService);
        Servlet admin = new AdminPageServlet(accountService, server);
        Servlet mainPage = new MainPageServlet();
        WebSocketServlet game = new WebSocketGameServlet(gameServices, gameUrlParameters);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setVirtualHosts(new String[]{host});

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
