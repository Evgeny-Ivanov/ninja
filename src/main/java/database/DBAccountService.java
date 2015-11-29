package database;

import base.AccountService;
import base.UserProfile;
import database.dao.UserProfileDao;
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
    @SuppressWarnings({"ConstantConditions", "FieldNameHidesFieldInSuperclass"})
    @NotNull
    static final Logger LOGGER = LogManager.getLogger(DBAccountService.class);

    @NotNull
    private final Map<String, UserProfile> sessions = new HashMap<>();

    @NotNull
    private UserProfileDao userProfileDao;

    public DBAccountService(@NotNull String configurationFileName) {
        super(configurationFileName);
        userProfileDao = new UserProfileDao(getConnection());
    }

    @Override
    public boolean addUser(@NotNull String userEmail, @NotNull UserProfile userProfile) {
        try {
            userProfileDao.insert(userProfile);
            return true;
        } catch (SQLException e) {
            LOGGER.warn("SQLException addUser ");
            LOGGER.warn(e);
            return false;
        }
    }

    @Override
    public void addSessions(@NotNull String sessionId, @NotNull UserProfile userProfile) {
        sessions.put(sessionId, userProfile);
    }

    @Nullable
    @Override
    public UserProfile getUser(@Nullable String userEmail) {
        if (userEmail == null) {
            return null;
        }
        try {
            return userProfileDao.readByEmail(userEmail);
        } catch (SQLException e) {
            LOGGER.warn("SQLException getUser ");
            LOGGER.warn(e);
            return null;
        }
    }

    @Nullable
    @Override
    public UserProfile getSessions(@Nullable String sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public int countUsers() {
        try {
            return userProfileDao.count();
        } catch (SQLException e) {
            LOGGER.warn("SQLException");
            return 0;
        }
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

    @Override
    public int deleteAllUsers() {
        try {
            return userProfileDao.deleteAll();
        } catch (SQLException e) {
            LOGGER.warn("SQLException");
            return 0;
        }
    }
}
