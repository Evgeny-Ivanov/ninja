package frontend;

import main.AccountService;
import main.UserProfile;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;

/**
 * Created by ilya on 23.10.15.
 */
public class SignUpServletTest  extends Mockito {
    @NotNull
    private final AccountService accountService = new AccountService();

    @SuppressWarnings("ConstantConditions")
    @NotNull
    private final HttpServletRequest request = mock(HttpServletRequest.class);

    @SuppressWarnings("ConstantConditions")
    @NotNull
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @NotNull
    private final SignUpServlet servlet = new SignUpServlet(accountService);

    private void testSignUpMethodPost(String name, String email, String password, String responseMessage) throws Exception {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        //noinspection ConstantConditions
        when(response.getWriter()).thenReturn(pw);

        //noinspection ConstantConditions
        when(request.getParameter("name")).thenReturn(name);
        //noinspection ConstantConditions
        when(request.getParameter("email")).thenReturn(email);
        //noinspection ConstantConditions
        when(request.getParameter("password")).thenReturn(password);

        servlet.doPost(request, response);

        //noinspection ConstantConditions
        verify(request, atLeastOnce()).getParameter("name");
        //noinspection ConstantConditions
        verify(request, atLeastOnce()).getParameter("email");
        //noinspection ConstantConditions
        verify(request, atLeastOnce()).getParameter("password");

        //noinspection ConstantConditions
        Assert.assertTrue(sw.toString().contains(responseMessage));
    }

    private void testSignUpMethodGet(Boolean bool, String strSessionId, String responseMessage) throws Exception {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        //noinspection ConstantConditions
        when(response.getWriter()).thenReturn(pw);

        //noinspection ConstantConditions
        when(request.getSession()).thenReturn(mock(HttpSession.class));

        //noinspection ConstantConditions
        when(request.getSession().getId()).thenReturn(strSessionId);

        servlet.doGet(request, response);

        //noinspection ConstantConditions
        verify(request.getSession(), atLeastOnce()).getId();

        //noinspection ConstantConditions
        assertEquals(bool, sw.toString().contains(responseMessage));

    }


    @Test
    public void testDoPost() throws Exception {
        testSignUpMethodPost("testName1", "testEmail1", "testPassword1", "New user created");
        testSignUpMethodPost("testName2", "testEmail1", "testPassword2", "already exists");
        testSignUpMethodPost(null, "testEmail1", "testPassword2", "Input error");
        testSignUpMethodPost("testName1", null, "testPassword1", "Input error");
        testSignUpMethodPost("testName1", "testEmail1", null, "Input error");
    }

    @Test
    public void testDoGet() throws Exception {
        String strSessionId = "sessionId-1";
        testSignUpMethodGet(false, strSessionId, "User already login");
        accountService.addSessions(strSessionId, new UserProfile("testname", "testemail", "testpassword"));
        testSignUpMethodGet(true, strSessionId, "User already login");
    }

}