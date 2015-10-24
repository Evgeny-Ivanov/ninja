package admin;

import main.AccountService;
import main.Main;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import static org.junit.Assert.*;

/**
 * Created by stalker on 24.10.15.
 */
public class AdminPageServletTest extends Mockito {

    private static final int PORT = 8080;
    @NotNull
    private final Server server = new Server(PORT);
    @NotNull
    private final AccountService accountService = new AccountService();
    @NotNull
    private final AdminPageServlet servlet = new AdminPageServlet(accountService,server);

    @SuppressWarnings("ConstantConditions")
    @NotNull
    private final HttpServletRequest request = mock(HttpServletRequest.class);

    @SuppressWarnings("ConstantConditions")
    @NotNull
    private final HttpServletResponse response  = mock(HttpServletResponse.class);

    @Test
    public void testDoGet() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(servlet), Main.ADMIN_PAGE_URL);
        server.setHandler(context);

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String timeString = "10";

        //noinspection ConstantConditions
        when(request.getParameter("shutdown")).thenReturn(timeString);

        servlet.doGet(request,response);
        assertTrue(server.isStopped());
    }

}