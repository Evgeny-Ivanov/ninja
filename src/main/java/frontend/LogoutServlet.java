package frontend;

import main.AccountService;
import org.jetbrains.annotations.NotNull;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ilya on 26.09.15.
 */
public class LogoutServlet extends HttpServlet {
    @NotNull
    private AccountService accountService;

    public LogoutServlet(@NotNull AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        Map<String, Object> pageVariables = new HashMap<>();

        try (PrintWriter pw = response.getWriter()) {
            if (pw != null) {
                if (accountService.deleteSessions(request.getSession().getId()) == null) {
                    pageVariables.put("logoutStatus", "User already logout");
                    pw.println(PageGenerator.getPage("logoutstatus.html", pageVariables));
                } else {
                    pageVariables.put("logoutStatus", "User logout success");
                    pw.println(PageGenerator.getPage("logoutstatus.html", pageVariables));
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
