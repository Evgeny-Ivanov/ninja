package database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.sql.*;

/**
 * Created by ilyap on 23.11.2015.
 */
public class DBExecutor {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger LOGGER = LogManager.getLogger(DBService.class);

    @NotNull
    Connection connection;

    public DBExecutor(@NotNull Connection connection) {
        this.connection = connection;
    }

    @Nullable
    public <T> T execQueryT(@NotNull String query,
                            @NotNull TResultHandler<T> handler)
            throws SQLException
    {
        T value;

        try (Statement stmt = connection.createStatement()) {

            if (stmt == null) {
                LOGGER.warn("stmt == null");
                return null;
            }

            try (ResultSet result = stmt.executeQuery(query)) {

                if (result == null) {
                    LOGGER.warn("result == null");
                    return null;
                }

                value = handler.handle(result);
            }
        }

        return value;
    }

    public void execQuery(@NotNull String query,
                          @NotNull TResultHandler handler)
            throws SQLException
    {
        try (Statement stmt = connection.createStatement()) {
            if (stmt == null) {
                LOGGER.warn("stmt == null");
                return;
            }
            try (ResultSet result = stmt.executeQuery(query)) {
                if (result == null) {
                    LOGGER.warn("result == null");
                    return;
                }
                handler.handle(result);
            }
        }
    }

    public int execUpdate(@NotNull String update) throws SQLException {
        int count = 0;
        try (Statement stmt = connection.createStatement()) {
            if (stmt == null) {
                LOGGER.warn("stmt == null");
                return count;
            }

            stmt.executeUpdate(update);
            count = stmt.getUpdateCount();
        }
        return count;
    }

}