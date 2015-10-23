package frontend;

import main.AccountService;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ilya on 23.10.15.
 */
public class SignInServletTest extends Mockito {
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

    @Test
    public void testDoGet() throws Exception {

    }

    @Test
    public void testDoPost() throws Exception {

    }
}