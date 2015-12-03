package database;

import base.AccountService;
import base.UserProfile;
import database.dao.ScoreDao;
import database.dao.UserDao;
import database.dataset.Score;
import database.dataset.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private UserDao userDao;
    @NotNull
    private ScoreDao scoreDao;

    public DBAccountService(@NotNull String configurationFileName) {
        super(configurationFileName);
        userDao = new UserDao(getConnection());
        scoreDao = new ScoreDao(getConnection());
    }

    @Override
    public boolean addUser(@NotNull String userEmail, @NotNull UserProfile userProfile) {
        try {
            User user = new User();
            user.setName(userProfile.getName());
            user.setPassword(userProfile.getPassword());
            user.setEmail(userProfile.getEmail());
            userDao.insert(user);
            return true;
        } catch (SQLException e) {
            LOGGER.warn("SQLException addUser ");
            LOGGER.warn(e);
            return false;
        }
    }

    @Override
    public boolean addScore(@NotNull String name, int scoreCount) {
        try {
            Score score = new Score();
            score.setName(name);
            score.setScore(scoreCount);
            scoreDao.insert(score);
            return true;
        } catch (SQLException e) {
            LOGGER.warn("SQLException v ");
            LOGGER.warn(e);
            return false;
        }
    }

    @Override
    public List<Score> getListScore(int amount) {
        try {
            return scoreDao.read(amount);
        } catch (SQLException e) {
            LOGGER.warn("SQLException v ");
            LOGGER.warn(e);
            return new ArrayList<>(0);
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
            User user = userDao.readByEmail(userEmail);
            if (user == null) {
                return null;
            }
            UserProfile up = new UserProfile();
            up.setEmail(user.getEmail());
            up.setName(user.getName());
            up.setPassword(user.getPassword());
            return up;
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
            return userDao.count();
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
            return userDao.deleteAll();
        } catch (SQLException e) {
            LOGGER.warn("SQLException");
            return 0;
        }
    }
}
