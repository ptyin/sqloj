package asia.ptyin.sqloj.engine.sql;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/***
 *
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
class QueryResultTest
{
    static Connection connection;

    @BeforeAll
    static void setup() throws SQLException
    {
        connection = DriverManager.getConnection("jdbc:sqlite::resource:db/Chinook.db");
    }

    @Test
    void toJson() throws SQLException, JsonProcessingException
    {
        connection.setAutoCommit(false);
        var result = SqlExecutor.execute(connection, """
                select * from albums where AlbumId < 10;
                """);
        System.out.println(result.toJson());
        connection.rollback();
    }
}