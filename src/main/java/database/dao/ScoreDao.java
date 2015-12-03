package database.dao;

import database.DBExecutor;
import database.dataset.Score;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilya on 29.11.15.
 */
public class ScoreDao {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    static final Logger LOGGER = LogManager.getLogger(ScoreDao.class);

    @NotNull
    private Connection connection;

    public ScoreDao(@NotNull Connection connection) {
        this.connection = connection;
    }

    public int insert(@NotNull Score score) throws SQLException {
        return DBExecutor.execUpdate(connection, "insert into score (name, score) " +
                "values ( '" + score.getName() + "' ,  "
                + score.getScore() + " )");
    }

    @NotNull
    public List<Score> read(int amount) throws SQLException {
        List<Score> listMain = DBExecutor.execQueryT(connection,
                "select * from score order by score desc limit 0 , " + amount + " ;",
                result -> {
                    List<Score> list = new ArrayList<>(amount);

                    while (result.next()) {
                        Score score = new Score();
                        //noinspection ConstantConditions
                        score.setName(result.getString("name"));
                        score.setScore(result.getInt("score"));
                        list.add(score);
                    }

                    return list;
                });
        if (listMain == null) {
            return new ArrayList<>(0);
        }
        return listMain;
    }

    public int count() throws SQLException {
        String query = "SELECT count(*) FROM score";
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
        return DBExecutor.execUpdate(connection, "delete from score ");
    }
}
