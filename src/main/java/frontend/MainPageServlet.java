package frontend;

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
 * Created by ilya on 26.09.15.
 */
public class MainPageServlet extends HttpServlet {
    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession hs = request.getSession();
        if (hs == null || hs.getId() == null) {
            return;
        } else if (hs.getAttribute("name") == null) {
            hs.setAttribute("name", "Incognitto");
        }

        try (PrintWriter pw = response.getWriter()) {
            if (pw != null) {
                pageVariables.put("name", hs.getAttribute("name"));
                pw.println(PageGenerator.getPage("mainpage.html", pageVariables));
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
