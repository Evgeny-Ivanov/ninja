package base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by v.chibrikov on 13.09.2014.
 */
public class AccountService {
    @NotNull
    private Map<String, UserProfile> users = new HashMap<>();
    @NotNull
    private Map<String, UserProfile> sessions = new HashMap<>();

    public boolean addUser(@NotNull String userEmail, @NotNull UserProfile userProfile) {
        if (users.containsKey(userEmail))
            return false;

        users.put(userEmail, userProfile);
        return true;
    }

    public void addSessions(@NotNull String sessionId, @NotNull UserProfile userProfile) {
        sessions.put(sessionId, userProfile);
    }

    @Nullable
    public UserProfile getUser(@Nullable String userEmail) {
        return users.get(userEmail);
    }

    @Nullable
    public UserProfile getSessions(@Nullable String sessionId) {
        return sessions.get(sessionId);
    }


    public int countUsers() {
        return users.size();
    }

    public int countSessions() {
        return sessions.size();
    }

    @Nullable
    public UserProfile deleteSessions(@Nullable String sessionId) {
        return sessions.remove(sessionId);
    }


    public void deleteAllSessions() {
        sessions.clear();
    }

    public void autoFullUsers() {
        UserProfile testUser1 = new UserProfile("Егор", "123", "test1@mail.ru");
        addUser(testUser1.getEmail(), testUser1);
        UserProfile testUser2 = new UserProfile("Илья", "123", "test2@mail.ru");
        addUser(testUser2.getEmail(), testUser2);
        UserProfile testUser3 = new UserProfile("Женя", "123", "test3@mail.ru");
        addUser(testUser3.getEmail(), testUser3);
        UserProfile testUser4 = new UserProfile("Дмитрий", "123", "test4@mail.ru");
        addUser(testUser4.getEmail(), testUser4);
        UserProfile testUser5 = new UserProfile("Константин", "123", "test5@mail.ru");
        addUser(testUser5.getEmail(), testUser5);
    }
}