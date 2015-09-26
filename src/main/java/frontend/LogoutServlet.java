package frontend;

import main.AccountService;
import main.UserProfile;
import org.jetbrains.annotations.NotNull;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
                HttpSession hs = request.getSession();
                if (accountService.deleteSessions(hs.getId()) == null) {
                    pageVariables.put("logoutStatus", "User already logout");
                    pw.println(PageGenerator.getPage("logoutstatus.html", pageVariables));
                } else {
                    pageVariables.put("logoutStatus", "User logout success");
                    pw.println(PageGenerator.getPage("logoutstatus.html", pageVariables));
                    hs.setAttribute("name", "Incognitto");
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
