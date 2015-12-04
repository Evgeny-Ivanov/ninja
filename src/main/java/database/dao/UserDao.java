package database.dao;

import database.DBExecutor;
import database.dataset.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by ilya on 29.11.15.
 */
public class UserDao {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger LOGGER = LogManager.getLogger(UserDao.class);

    @NotNull
    private Connection connection;

    public UserDao(@NotNull Connection connection) {
        this.connection = connection;
    }

    public int insert(@NotNull User user) throws SQLException {
        String query = "insert into user (name, password, email) " +
                "values ( '" + user.getName() + "' , '"
                + user.getPassword() + "' , "
                + " '" + user.getEmail() + "' )";
        LOGGER.info(query);
        return DBExecutor.execUpdate(connection, query);
    }

    @Nullable
    public User readByEmail(String email) throws SQLException {
        return DBExecutor.execQueryT(connection,
                "select * from user where email = '" + email + "' ",
                result -> {
                    User user = null;
                    if (result.next()) {
                        user = new User();

                        //noinspection ConstantConditions
                        user.setName(result.getString("name"));
                        //noinspection ConstantConditions
                        user.setPassword(result.getString("password"));
                        //noinspection ConstantConditions
                        user.setEmail(result.getString("email"));
                    }

                    return user;
                });
    }

    public int count() throws SQLException {
        String query = "SELECT count(*) FROM user";
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
        return DBExecutor.execUpdate(connection, "delete from user ");
    }
}
