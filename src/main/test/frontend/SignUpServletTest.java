package frontend;

import main.AccountService;
import main.UserProfile;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
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

    @NotNull
    private final UserProfile testUser = new UserProfile("testLogin", "testPassword", "test@mail.ru");

    @NotNull
    private final String testSessionId = "sissionId-1";

    @Before
    public void setUp() throws Exception {
        accountService.deleteAllSessions();
    }

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
        assertTrue(sw.toString().contains(responseMessage));
    }

    private void testSignUpMethodGet(String strSessionId, String responseMessage, Boolean isResponseMessage) throws Exception {
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

    @Test
    public void testDoPost() throws Exception {
        testSignUpMethodPost(testUser.getName(), testUser.getEmail(), testUser.getPassword(), "New user created");
    }

    @Test
    public void testDoPostReplayEmail() throws Exception {
        accountService.addUser(testUser.getEmail(), testUser);
        testSignUpMethodPost("testName2", testUser.getEmail(), "testPassword2", "already exists");
    }


    @Test
    public void testDoPostNullName() throws Exception {
        testSignUpMethodPost(null, "testEmail1", "testPassword2", "Input error");
    }

    @Test
    public void testDoPostNullEmail() throws Exception {
        testSignUpMethodPost("testName1", null, "testPassword1", "Input error");
    }

    @Test
    public void testDoPostNullPassword() throws Exception {
        testSignUpMethodPost("testName1", "testEmail1", null, "Input error");
    }

    @Test
    public void testDoGet() throws Exception {
        testSignUpMethodGet(testSessionId, "User already login", false);
    }

    @Test
    public void testDoGetReplayLogin() throws Exception {
        accountService.addSessions(testSessionId, testUser);
        testSignUpMethodGet(testSessionId, "User already login", true);
    }

}