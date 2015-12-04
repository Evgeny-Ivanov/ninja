package frontend;

import base.AccountService;
import database.dataset.Score;
import game.GameMessager;
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
import java.util.List;
import java.util.Map;

/**
 * Created by ilya on 26.09.15.
 */
public class ScoresServlet extends HttpServlet {

    private static final int DEFAULT_AMOUNT = 100;
    @NotNull
    private final AccountService accountService;

    public ScoresServlet(@NotNull AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        HttpSession hs = request.getSession();
        if (hs == null || hs.getId() == null) {
            return;
        }

        Integer amount = DEFAULT_AMOUNT;
        String amountstr = request.getParameter("limit");
        if (amountstr != null) {
            amount = new Integer(amountstr);
        }
        if (amount < 1) {
            amount = DEFAULT_AMOUNT;
        }

        try (PrintWriter pw = response.getWriter()) {
            if (pw != null) {
                pw.println(GameMessager.createMessageListScores(accountService, amount));
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
