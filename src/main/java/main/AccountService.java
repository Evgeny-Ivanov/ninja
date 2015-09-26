package main;

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
    public UserProfile getSessions(String sessionId) {
        return sessions.get(sessionId);
    }

    @Nullable
    public UserProfile deleteSessions(String sessionId) {
        return sessions.remove(sessionId);
    }

}
