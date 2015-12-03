package base;

import database.dataset.Score;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by ilya on 29.11.15.
 */
public interface AccountService {
    boolean addUser(@NotNull String userEmail, @NotNull UserProfile userProfile);
    List<Score> getListScore(int amount);
    boolean addScore(@NotNull String name, int scoreCount);
    void addSessions(@NotNull String sessionId, @NotNull UserProfile userProfile);
    UserProfile getUser(@Nullable String userEmail);
    UserProfile getSessions(@Nullable String sessionId);
    int countUsers();
    int countSessions();
    UserProfile deleteSessions(@Nullable String sessionId);
    void deleteAllSessions();
    int deleteAllUsers();
}
