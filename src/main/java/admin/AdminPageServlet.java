package admin;
import helper.TimeHelper;
import main.AccountService;
import org.jetbrains.annotations.NotNull;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminPageServlet extends HttpServlet {
    public static final String ADMIN_PAGE_URL = "/admin";
    @NotNull
    private AccountService accountService;

    public AdminPageServlet(@NotNull AccountService aS){
        accountService = aS;
    }

    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();

        String timeString = request.getParameter("shutdown");
        if (timeString != null) {
            int timeMS = Integer.valueOf(timeString);
            System.out.print("Server will be down after: "+ timeMS + " ms");
            TimeHelper.sleep(timeMS);
            System.out.print("\nShutdown");
            System.exit(0);
        }

        pageVariables.put("status", "run");
        pageVariables.put("countUser",accountService.countUsers());
        pageVariables.put("countSession",accountService.countSessions());

        response.getWriter().println(PageGenerator.getPage("admin/admin.tml", pageVariables));

    }



}

