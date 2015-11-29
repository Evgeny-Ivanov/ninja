package database.dao;

import base.UserProfile;
import database.DBExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by ilya on 29.11.15.
 */
public class UserProfileDao {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger LOGGER = LogManager.getLogger(UserProfileDao.class);

    @NotNull
    private Connection connection;

    public UserProfileDao(@NotNull Connection connection) {
        this.connection = connection;
    }

    public int insert(@NotNull UserProfile userProfile) throws SQLException {
        return DBExecutor.execUpdate(connection, "insert into userprofile (name, password, email) " +
                "values ( '" + userProfile.getName() + "' , '"
                + userProfile.getPassword() + "' , "
                + " '" + userProfile.getEmail() + "' )");
    }

    @Nullable
    public UserProfile readByEmail(String email) throws SQLException {
        return DBExecutor.execQueryT(connection,
                "select * from userprofile where email = '" + email + "' ",
                result -> {
                    UserProfile up = null;
                    if (result.next()) {
                        if (result.getString("name") == null ||
                                result.getString("email") == null ||
                                result.getString("password") == null) {
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
    }

    public int count() throws SQLException {
        String query = "SELECT count(*) FROM userprofile";
        return  DBExecutor.execQueryT(connection, query, result -> {

            if (result.next()) {
                return result.getInt(1);
            } else {
                LOGGER.warn("count() result == null");
                return 0;
            }
        });
    }

    public int deleteAll() throws SQLException {
        return DBExecutor.execUpdate(connection, "delete from userprofile ");
    }
}
