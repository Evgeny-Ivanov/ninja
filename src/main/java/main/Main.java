package main;

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
    static final int DEFAULT_PORT = 8080;

    public static void main(@NotNull String[] args) throws Exception {
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
        Servlet logout = new LogoutServlet(accountService);
        Servlet mainPage = new MainPageServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(signIn), "/api/v1/auth/signin");
        context.addServlet(new ServletHolder(signUp), "/api/v1/auth/signup");
        context.addServlet(new ServletHolder(logout), "/api/v1/auth/logout");
        context.addServlet(new ServletHolder(mainPage), "/mainpage");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(port);
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}