package database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

/**
 * Created by ilyap on 23.11.2015.
 */
public class DBService {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger LOGGER = LogManager.getLogger(DBService.class);

    private Connection connection;
    @NotNull
    private DBConfiguration dbConfiguration;

    public DBService(@NotNull String configurationFileName) {
        dbConfiguration = new DBConfiguration(configurationFileName);
        openConnection();
    }

    @NotNull
    public Connection getConnection() {
        if (connection == null) {
            LOGGER.error("connection == null");
            throw new NullPointerException();
        }
        return connection;
    }

    @NotNull
    private Connection openConnection() {
        String nameDriver = dbConfiguration.getNameDriver();
        String jdbcUrl = dbConfiguration.getJdbcUrl();
        String userName = dbConfiguration.getUserName();
        String password = dbConfiguration.getPassword();

        try {
            @SuppressWarnings("ConstantConditions")
            Driver driver = (Driver) Class.forName(nameDriver).newInstance();

            if (driver == null) {
                LOGGER.error("driver == null");
                throw new NullPointerException();
            }
            DriverManager.registerDriver(driver);

            connection = DriverManager.getConnection(jdbcUrl, userName, password);
        } catch (ClassNotFoundException | SQLException | IllegalAccessException | InstantiationException ignored) {
            LOGGER.error(ignored);
            throw new RuntimeException();
        }

        if (isEmpty()) {
            LOGGER.error("connection == null");
            throw new NullPointerException();
        }

        LOGGER.info(printInfoOfConnection());
        //noinspection ConstantConditions
        return connection;
    }

    public boolean isEmpty() {
        return connection == null;
    }

    public boolean closeConnection() {
        if (isEmpty()) {
            LOGGER.error("connection == null");
            throw new NullPointerException();
        }

        try {
            //noinspection ConstantConditions
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    public StringBuilder  printInfoOfConnection() {
        if (isEmpty()) {
            LOGGER.error("connection == null");
            throw new NullPointerException();
        }

        StringBuilder str = new StringBuilder();
        try {
            str.append("Autocommit: ").append(connection.getAutoCommit()).append('\n');
            str.append("DB name: ").append(connection.getMetaData().getDatabaseProductName()).append('\n');
            str.append("DB version: ").append(connection.getMetaData().getDatabaseProductVersion()).append('\n');
            str.append("Driver name: ").append(connection.getMetaData().getDriverName()).append('\n');
            str.append("Driver version: ").append(connection.getMetaData().getDriverVersion()).append('\n');
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return str;
    }

}
