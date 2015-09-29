package main;


import admin.AdminPageServlet;
import frontend.LogoutServlet;
import frontend.MainPageServlet;
import frontend.SignInServlet;
import frontend.SignUpServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jetbrains.annotations.NotNull;

import javax.servlet.Servlet;

/**
 * @author v.chibrikov
 */
public class Main {
    public static final String ADMIN_PAGE_URL = "/admin";
    public static final String SIGNIN_PAGE_URL = "/api/v1/auth/signin";
    public static final String SIGNUP_PAGE_URL = "/api/v1/auth/signup";
    public static final String LOGOUT_PAGE_URL = "/api/v1/auth/logout";
    public static final String MAINPAGE_PAGE_URL = "/mainpage";

    static final int DEFAULT_PORT = 8080;

    public static void main(@NotNull String[] args) {
        int port = DEFAULT_PORT;

        if (args.length == 1 && args[0] != null) {
            Integer newport = Integer.valueOf(args[0]);
            if (newport != null) {
                port = newport;
            }
        }

        System.out.println("Starting at port: " + String.valueOf(port));

        AccountService accountService = new AccountService();

        Servlet signIn = new SignInServlet(accountService);
        Servlet signUp = new SignUpServlet(accountService);
        Servlet admin = new AdminPageServlet(accountService);
        Servlet logout = new LogoutServlet(accountService);
        Servlet mainPage = new MainPageServlet();


        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(signIn), SIGNIN_PAGE_URL);
        context.addServlet(new ServletHolder(signUp), SIGNUP_PAGE_URL);
        context.addServlet(new ServletHolder(admin), ADMIN_PAGE_URL);
        context.addServlet(new ServletHolder(logout), LOGOUT_PAGE_URL);
        context.addServlet(new ServletHolder(mainPage), MAINPAGE_PAGE_URL);

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(port);
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
