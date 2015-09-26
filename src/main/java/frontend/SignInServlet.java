package frontend;

import main.AccountService;
import main.UserProfile;
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

        try (PrintWriter pw = response.getWriter()) {
            if (pw != null) {
                if (accountService.getSessions(request.getSession().getId()) != null) {
                    pageVariables.put("signInStatus", "User already login");
                    pw.println(PageGenerator.getPage("signinstatus.html", pageVariables));;
                } else {
                    pw.println(PageGenerator.getPage("signin.html", pageVariables));
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Map<String, Object> pageVariables = new HashMap<>();
//        pageVariables.put("email", email == null ? "" : email);
//        pageVariables.put("password", password == null ? "" : password);
//
//        try (PrintWriter pw = response.getWriter()) {
//            if (pw != null) {
//                pw.println(PageGenerator.getPage("authresponse.txt", pageVariables));
//            }
//        }

        if (password == null || email == null) {
            pageVariables.put("signInStatus", "Input error");
        } else {
            UserProfile profile = accountService.getUser(email);

            if (profile != null && profile.getPassword().equals(password)) {
                accountService.addSessions(request.getSession().getId(), profile);
                pageVariables.put("signInStatus", "Success login");
            } else {
                pageVariables.put("signInStatus", "Password error or user not found");
            }
        }

        try (PrintWriter pw = response.getWriter()) {
            if (pw != null) {
                pw.println(PageGenerator.getPage("signinstatus.html", pageVariables));
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
