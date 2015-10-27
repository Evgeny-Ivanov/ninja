package frontend;

import base.AccountService;
import base.UserProfile;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;

/**
 * Created by ilya on 24.10.15.
 */
public class LogoutServletTest extends Mockito {
    @NotNull
    private final AccountService accountService = new AccountService();

    @SuppressWarnings("ConstantConditions")
    @NotNull
    private final HttpServletRequest request = mock(HttpServletRequest.class);

    @SuppressWarnings("ConstantConditions")
    @NotNull
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @NotNull
    private final LogoutServlet servlet = new LogoutServlet(accountService);

    @NotNull
    private final UserProfile testUser = new UserProfile("testLogin", "testPassword", "test@mail.ru");

    @NotNull
    private final String testSessionId = "sissionId-1";

    @Before
    public void setUp() throws Exception {
        accountService.deleteAllSessions();
    }

    private void testLogoutMethodGet(String strSessionId, String responseMessage) throws Exception {
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
        assertTrue(sw.toString().contains(responseMessage));
    }

    @Test
    public void testDoGetNonExists() throws Exception {
        testLogoutMethodGet(testSessionId, "User already logout");
    }

    @Test
    public void testDoGet() throws Exception {
        accountService.addSessions(testSessionId, testUser);
        testLogoutMethodGet(testSessionId, "User logout success");
    }

    @Test
    public void testDoGetIncorrectSessionId() throws Exception {
        accountService.addSessions(testSessionId, testUser);
        testLogoutMethodGet(testSessionId + "!", "User already logout");
    }
}