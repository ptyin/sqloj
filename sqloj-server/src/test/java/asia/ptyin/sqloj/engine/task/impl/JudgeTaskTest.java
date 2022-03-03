package asia.ptyin.sqloj.engine.task.impl;

import asia.ptyin.sqloj.engine.sql.SqlExecutionUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


@Log4j2
class JudgeTaskTest
{
    static Connection connection;

    @BeforeAll
    static void setup() throws SQLException
    {
        connection = DriverManager.getConnection("jdbc:sqlite::resource:db/Chinook.db");
        connection.setAutoCommit(false);
    }

    @Test
    void run() throws SQLException, InterruptedException
    {
        var submit = """
                select title
                from albums natural join tracks
                where mediatypeid not in (select mediatypeid from media_types where name = 'MPEG audio file')
                """;
        var answer = """
                select distinct title
                from albums natural join tracks
                where mediatypeid not in (select mediatypeid from media_types where name = 'MPEG audio file')
                """;
        var criterion = SqlExecutionUtils.execute(connection, answer);
        var task = new JudgeTask(connection, submit, criterion);
        var result = task.run();
        log.debug(result.serialize());
        assertFalse(result.isPass());
        assertTrue(result.getComments().containsKey("redundant rows"));
    }
}