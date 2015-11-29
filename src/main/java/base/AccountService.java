package base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by ilya on 29.11.15.
 */
public interface AccountService {
    boolean addUser(@NotNull String userEmail, @NotNull UserProfile userProfile);
    void addSessions(@NotNull String sessionId, @NotNull UserProfile userProfile);
    UserProfile getUser(@Nullable String userEmail);
    UserProfile getSessions(@Nullable String sessionId);
    int countUsers();
    int countSessions();
    UserProfile deleteSessions(@Nullable String sessionId);
    void deleteAllSessions();
    int deleteAllUsers();
}
