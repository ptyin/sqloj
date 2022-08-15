package net.ptyin.sqloj.engine.sql;

import lombok.extern.log4j.Log4j2;
import net.ptyin.sqloj.engine.result.ExecutionResult;
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
        String sql = "\n" +
                "-- In line comment; --\n " +
                "select 'haha;--' as haha, Title from Albums where AlbumId = 1;\n" +
                "/* \n" +
                "Start block comment; \n" +
                "select Title as name from Albums; \n" +
                "End block comment; \n" +
                "*/ \n" +
                "select Title from Albums where AlbumId = 1;\n";
        ExecutionResult results = SqlExecutionUtils.execute(connection, sql);
        log.debug(results.getQueryResult(0).serialize());
        log.debug(results.getQueryResult(1).serialize());
        Assertions.assertEquals("haha;--", results.getQueryResult(0).getRows().get(0).get(0));
        Assertions.assertEquals("Title",
                results.getQueryResult(1).getMetadata().getColumnMetadata(0).getColumnLabel());
    }
}