package database;

import base.AccountService;
import base.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ilya on 29.11.15.
 */
public class DBAccountService  extends DBService  implements AccountService {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger LOGGER = LogManager.getLogger(DBAccountService.class);

    @NotNull
    private final Map<String, UserProfile> sessions = new HashMap<>();

    public DBAccountService(@NotNull String configurationFileName) {
        super(configurationFileName);
    }

    @Override
    public boolean addUser(@NotNull String userEmail, @NotNull UserProfile userProfile) {
        try {
            getExecutor().execUpdate("insert into userprofile (name, password, email) " +
                    "values ( '" + userProfile.getName() + "' , '"
                    + userProfile.getPassword() + "' , "
                    + " '" +userProfile.getEmail() +"' )");

            return true;
        } catch (SQLException e) {
            LOGGER.warn("SQLException addUser ");
            LOGGER.warn(e);
        }

        return false;
    }

    @Override
    public void addSessions(@NotNull String sessionId, @NotNull UserProfile userProfile) {
        sessions.put(sessionId, userProfile);
    }

    @Nullable
    @Override
    public UserProfile getUser(@Nullable String userEmail) {
        UserProfile userProfile = null;

        try {
            userProfile = getExecutor().execQueryT("select * from userprofile where email = '" + userEmail + "' ", result -> {
                UserProfile up = null;
                if (result.next()) {
                    if (result.getString("name") == null ||
                            result.getString("email") == null ||
                            result.getString("password") == null)
                    {
                        LOGGER.warn("result.### == null");
                        return null;
                    }

                    //noinspection ConstantConditions
                    up = new UserProfile(result.getString("name"),
                            result.getString("password"),
                            result.getString("email"));
                }

                return up;
            });

        } catch (SQLException e) {
            LOGGER.warn("SQLException getUser ");
            LOGGER.warn(e);
        }

        return userProfile;
    }

    @Nullable
    @Override
    public UserProfile getSessions(@Nullable String sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public int countUsers() {
        int count = 0;
        try {
            String query = "SELECT count(*) FROM userprofile";
            count = getExecutor().execQueryT(query, result -> {
                result.next();
                return result.getInt(1);
            });

        } catch (SQLException e) {
            LOGGER.warn("SQLException");
        }
        return count;
    }

    @Override
    public int countSessions() {
        return sessions.size();
    }

    @Nullable
    @Override
    public UserProfile deleteSessions(@Nullable String sessionId) {
        return sessions.remove(sessionId);
    }

    @Override
    public void deleteAllSessions() {
        sessions.clear();
    }
}
