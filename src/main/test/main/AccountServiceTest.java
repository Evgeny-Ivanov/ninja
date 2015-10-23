package main;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ilya on 22.10.15.
 */
public class AccountServiceTest {
    @NotNull
    private final AccountService accountService = new AccountService();

    @NotNull
    private final UserProfile testUser = new UserProfile("testLogin", "testPassword", "test@mail.ru");
    @NotNull
    private final UserProfile testUser1 = new UserProfile("testLogin", "testPassword", "test@mail.ru");
    @NotNull
    private final UserProfile testUser2 = new UserProfile("testLogin2", "testPassword2", "test2@mail.ru");

    @NotNull
    private final String testSessionId = "sissionId-1";

    @Test
    public void testAddUser() throws Exception {
        assertTrue(accountService.addUser(testUser.getEmail(), testUser));

        final UserProfile user = accountService.getUser(testUser.getEmail());

        assertNotNull(user);
        assertEquals(testUser.getEmail(), user.getEmail());
        assertEquals(testUser.getName(), user.getName());
        assertEquals(testUser.getPassword(), user.getPassword());

        assertTrue(!accountService.addUser(testUser.getEmail(), testUser));
        assertTrue(!accountService.addUser(testUser1.getEmail(), testUser1));

    }

    @Test
    public void testAddSessions() throws Exception {
        accountService.addSessions(testSessionId, testUser);

        final UserProfile user = accountService.getSessions(testSessionId);

        assertNotNull(user);
        assertEquals(testUser.getEmail(), user.getEmail());
        assertEquals(testUser.getName(), user.getName());
        assertEquals(testUser.getPassword(), user.getPassword());
    }

    @Test
    public void testGetUser() throws Exception {

    }

    @Test
    public void testGetSessions() throws Exception {

    }

    @Test
    public void testCountUsers() throws Exception {
        int startCountUsers = accountService.countUsers();
        int perfectFinishCountUsers = accountService.countUsers();
        if (accountService.addUser(testUser2.getEmail(), testUser2) ) {
            perfectFinishCountUsers += 1;
        }
        assertEquals(accountService.countUsers(), perfectFinishCountUsers);

        startCountUsers = accountService.countUsers();
        perfectFinishCountUsers = accountService.countUsers();
        if (accountService.addUser(testUser2.getEmail(), testUser2) ) {
            perfectFinishCountUsers += 1;
        }
        assertEquals(accountService.countUsers(), perfectFinishCountUsers);
    }

    @Test
    public void testCountSessions() throws Exception {
        String testSessionId2 = "sissionId-2";

        accountService.deleteSessions(testSessionId2);
        int startCountSessions = accountService.countSessions();
        accountService.addSessions(testSessionId2, testUser2);
        assertEquals(startCountSessions + 1, accountService.countSessions());

        startCountSessions = accountService.countSessions();
        accountService.addSessions(testSessionId2, testUser2);
        assertEquals(startCountSessions, accountService.countSessions());
    }

    @Test
    public void testDeleteSessions() throws Exception {
        accountService.addSessions(testSessionId, testUser);
        final UserProfile userDeleted = accountService.deleteSessions(testSessionId);

        assertNotNull(userDeleted);
        assertEquals(testUser.getEmail(), userDeleted.getEmail());
        assertEquals(testUser.getName(), userDeleted.getName());
        assertEquals(testUser.getPassword(), userDeleted.getPassword());

        assertNull(accountService.deleteSessions(testSessionId));
    }
}