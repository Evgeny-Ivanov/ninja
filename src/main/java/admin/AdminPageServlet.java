package admin;

import base.AccountService;
import utils.TimeHelper;
import base.AccountServiceImpl;
import org.eclipse.jetty.server.Server;
import org.jetbrains.annotations.NotNull;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class AdminPageServlet extends HttpServlet {

    @NotNull
    private final AccountService accountService;

    @NotNull
    private final Server server;

    public AdminPageServlet(@NotNull AccountService accountService, @NotNull Server server) {
        this.accountService = accountService;
        this.server = server;
    }

    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        Map<String, Object> pageVariables = new HashMap<>();

        String timeString = request.getParameter("shutdown");
        if (timeString != null) {
            Integer timeMS = Integer.valueOf(timeString);
            if (timeMS == null) {
                return;
            }
            System.out.print("Server will be down after: " + timeMS + " ms");
            TimeHelper.sleep(timeMS);
            System.out.print("\nShutdown");
            try {
                server.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        pageVariables.put("status", "run");
        pageVariables.put("countUser", accountService.countUsers());
        pageVariables.put("countSession", accountService.countSessions());

        try (PrintWriter pw = response.getWriter()) {
            if (pw != null) {
                pw.println(PageGenerator.getPage("admin/admin.tml", pageVariables));
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}

