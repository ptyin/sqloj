package asia.ptyin.sqloj.engine.sql;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Log4j2
class SqlExecutionUtilsTest
{
    static Connection connection;

    @BeforeAll
    static void setup() throws SQLException
    {
        connection = DriverManager.getConnection("jdbc:sqlite::resource:db/Chinook.db");
    }

    @Test
    void execute() throws Exception
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
                select Title from Albums where AlbumId = 1;
                
                """;
        var results = SqlExecutionUtils.execute(connection, sql);
        log.debug(results.get(0).serialize());
        log.debug(results.get(1).serialize());
        Assertions.assertEquals("haha;--", results.get(0).getRows().get(0).get(0));
        Assertions.assertEquals("Title", results.get(1).getMetadata().getColumnMetadata(0).getColumnLabel());
    }
}