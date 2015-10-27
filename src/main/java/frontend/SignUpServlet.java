package frontend;

import base.AccountService;
import base.UserProfile;
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
 * Created by v.chibrikov on 13.09.2014.
 */
public class SignUpServlet extends HttpServlet {
    @NotNull
    private AccountService accountService;

    public SignUpServlet(@NotNull AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Map<String, Object> pageVariables = new HashMap<>();

        if (name == null || password == null || email == null) {
            pageVariables.put("signUpStatus", "Input error");
        } else if (accountService.addUser(email, new UserProfile(name, password, email))) {
            pageVariables.put("signUpStatus", "New user created");
        } else {
            pageVariables.put("signUpStatus", "User with name: " + name + " already exists");
        }

        try (PrintWriter pw = response.getWriter()) {
            if (pw != null) {
                pw.println(PageGenerator.getPage("auth/signupstatus.html", pageVariables));
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
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
                    pageVariables.put("signUpStatus", "User already login");
                    pw.println(PageGenerator.getPage("auth/signupstatus.html", pageVariables));
                } else {
                    pw.println(PageGenerator.getPage("auth/signup.html", pageVariables));
                }
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
