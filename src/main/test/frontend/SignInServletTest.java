package frontend;

import base.AccountServiceImpl;
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
 * Created by ilya on 23.10.15.
 */
public class SignInServletTest extends Mockito {
    @NotNull
    private final AccountServiceImpl accountService = new AccountServiceImpl();

    @SuppressWarnings("ConstantConditions")
    @NotNull
    private final HttpServletRequest request = mock(HttpServletRequest.class);

    @SuppressWarnings("ConstantConditions")
    @NotNull
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @NotNull
    private final SignInServlet servlet = new SignInServlet(accountService);

    @NotNull
    private final UserProfile testUser = new UserProfile("testLogin", "testPassword", "test@mail.ru");

    @NotNull
    private final String testSessionId = "sissionId-1";

    @Before
    public void setUp() throws Exception {
        accountService.deleteAllSessions();
    }


    private void testSignInMethodGet(String strSessionId, String responseMessage, Boolean isResponseMessage) throws Exception {
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
        assertEquals(isResponseMessage, sw.toString().contains(responseMessage));
    }

    private void testSignInMethodPost(String strSessionId, String email, String password, String responseMessage) throws Exception {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        //noinspection ConstantConditions
        when(response.getWriter()).thenReturn(pw);

        //noinspection ConstantConditions
        when(request.getParameter("email")).thenReturn(email);
        //noinspection ConstantConditions
        when(request.getParameter("password")).thenReturn(password);
        //noinspection ConstantConditions
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        //noinspection ConstantConditions
        when(request.getSession().getId()).thenReturn(strSessionId);

        servlet.doPost(request, response);

        //noinspection ConstantConditions
        verify(request, atLeastOnce()).getParameter("email");
        //noinspection ConstantConditions
        verify(request, atLeastOnce()).getParameter("password");

        //noinspection ConstantConditions
        assertTrue(sw.toString().contains(responseMessage));
    }


    @Test
    public void testDoGet() throws Exception {
        testSignInMethodGet(testSessionId, "User already login", false);
    }

    @Test
    public void testDoGetReplayLogin() throws Exception {
        accountService.addSessions(testSessionId, testUser);
        testSignInMethodGet(testSessionId, "User already login", true);
    }

    @Test
    public void testDoPost() throws Exception {
        accountService.addUser(testUser.getEmail(), testUser);
        testSignInMethodPost(testSessionId, testUser.getEmail(), testUser.getPassword(), "Success login");
    }

    @Test
    public void testDoPostUserNotFound() throws Exception {
        accountService.addUser(testUser.getEmail() + "!", testUser);
        testSignInMethodPost(testSessionId, testUser.getEmail(), testUser.getPassword(), "user not found");
    }

    @Test
    public void testDoPostIncorrectPassword() throws Exception {
        accountService.addUser(testUser.getEmail(), testUser);
        testSignInMethodPost(testSessionId, testUser.getEmail(), testUser.getPassword() + "!", "Password error");
    }
}