package frontend;

import main.AccountService;
import main.UserProfile;
import org.jetbrains.annotations.NotNull;
import utils.PageGenerator;

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
 * @author v.chibrikov
 */
public class SignInServlet extends HttpServlet {
    @NotNull
    private AccountService accountService;

    public SignInServlet(@NotNull AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession hs = request.getSession();
        if (hs == null || hs.getId() == null) {
            return;
        }

        try (PrintWriter pw = response.getWriter()) {
            if (pw != null) {
                if (accountService.getSessions(hs.getId()) != null) {
                    pageVariables.put("signInStatus", "User already login");
                    pw.println(PageGenerator.getPage("auth/signinstatus.html", pageVariables));
                } else {
                    pw.println(PageGenerator.getPage("auth/signin.html", pageVariables));
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession hs = request.getSession();
        if (hs == null || hs.getId() == null) {
            return;
        }

        if (password == null || email == null) {
            pageVariables.put("signInStatus", "Input error");
        } else {
            UserProfile profile = accountService.getUser(email);
            if (profile != null && profile.getPassword().equals(password)) {
                accountService.addSessions(hs.getId(), profile);
                pageVariables.put("signInStatus", "Success login");
                hs.setAttribute("name", profile.getName());
                pageVariables.put("name", hs.getAttribute("name"));
            } else {
                pageVariables.put("signInStatus", "Password error or user not found");
            }
        }

        try (PrintWriter pw = response.getWriter()) {
            if (pw != null) {
                pw.println(PageGenerator.getPage("auth/signinstatus.html", pageVariables));
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
