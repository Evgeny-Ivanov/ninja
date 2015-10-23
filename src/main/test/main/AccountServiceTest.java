package main;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ilya on 22.10.15.
 */
public class AccountServiceTest {
    @NotNull
    private AccountService accountService = new AccountService();

    @NotNull
    private final UserProfile testUser = new UserProfile("testLogin", "testPassword", "test@mail.ru");

    @NotNull
    private final String testSessionId = "sissionId-1";

    @Before
    public void setUp() throws Exception {
        accountService = new AccountService();
    }

    @Test
    public void testAddUserFirst() throws Exception {
        assertTrue(accountService.addUser(testUser.getEmail(), testUser));

        final UserProfile user = accountService.getUser(testUser.getEmail());

        assertNotNull(user);
        assertEquals(testUser.getEmail(), user.getEmail());
        assertEquals(testUser.getName(), user.getName());
        assertEquals(testUser.getPassword(), user.getPassword());
    }

    @Test
    public void testAddUserDouble() throws Exception {
        accountService.addUser(testUser.getEmail(), testUser);
        assertFalse(accountService.addUser(testUser.getEmail(), testUser));
    }

    @Test
    public void testAddUserIdenticalEmail() throws Exception {
        accountService.addUser(testUser.getEmail(), testUser);
        UserProfile testUser1 = new UserProfile("newUser", "newPassword", testUser.getEmail());
        assertFalse(accountService.addUser(testUser1.getEmail(), testUser1));
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
    public void testCountUsersAdd() throws Exception {
        int startCountUsers = accountService.countUsers();
        accountService.addUser(testUser.getEmail(), testUser);
        assertEquals(accountService.countUsers(), startCountUsers + 1);
    }

    @Test
    public void testCountUsersReplay() throws Exception {
        accountService.addUser(testUser.getEmail(), testUser);
        int startCountUsers = accountService.countUsers();
        UserProfile testUser2 = new UserProfile("testLogin", "testPassword", testUser.getEmail());
        accountService.addUser(testUser2.getEmail(), testUser2);
        assertEquals(accountService.countUsers(), startCountUsers);
    }

    @Test
    public void testCountSessionsAdd() throws Exception {
        int startCountSessions = accountService.countSessions();
        accountService.addSessions(testSessionId, testUser);
        assertEquals(startCountSessions + 1, accountService.countSessions());
    }

    @Test
    public void testCountSessionsReplay() throws Exception {
        accountService.addSessions(testSessionId, testUser);

        UserProfile testUser2 = new UserProfile("testLogin2", "testPassword2", "testEmail2");

        int startCountSessions = accountService.countSessions();
        accountService.addSessions(testSessionId, testUser2);
        assertEquals(startCountSessions, accountService.countSessions());
    }

    @Test
    public void testCountSessionsDelete() throws Exception {
        accountService.addSessions(testSessionId, testUser);
        int startCountSessions = accountService.countSessions();

        accountService.deleteSessions(testSessionId);
        assertEquals(startCountSessions - 1, accountService.countSessions());
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

    @Test
    public void testDeleteSessionsNonExistent() throws Exception {
        assertNull(accountService.deleteSessions(testSessionId));
    }
}