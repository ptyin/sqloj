package asia.ptyin.sqloj.engine.sql;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Log4j2
class SqlExecutorTest
{
    static Connection connection;

    @BeforeAll
    static void setup() throws SQLException
    {
        connection = DriverManager.getConnection("jdbc:sqlite::resource:db/Chinook.db");
    }

    @Test
    void execute() throws SQLException, JsonProcessingException
    {
        @SuppressWarnings("SqlDialectInspection")
        var sql = """
                -- In line comment; --
                select 'haha;--' as haha, Title from Albums where AlbumId = 1;
                /*
                Start block comment;
                select Title as name from Albums;
                End block comment;
                */
                select Title as name from Albums where AlbumId = 1;
                
                """;
        var result = SqlExecutor.execute(connection, sql);
        log.debug(result.toJson());
        Assertions.assertEquals("haha;--", result.getRows().get(0).get(0));
    }
}