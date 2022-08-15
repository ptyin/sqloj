package net.ptyin.sqloj.engine.comparators;

import lombok.extern.log4j.Log4j2;
import net.ptyin.sqloj.engine.result.ExecutionResult;
import net.ptyin.sqloj.engine.sql.SqlExecutionUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/***
 *
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Log4j2
class RowComparatorTest
{

    static Connection connection;

    @BeforeAll
    static void setup() throws SQLException
    {
        connection = DriverManager.getConnection("jdbc:sqlite::resource:db/Chinook.db");
        connection.setAutoCommit(false);
    }
    @Test
    void compare() throws Exception
    {
        ExecutionResult criterion = SqlExecutionUtils.execute(connection, "select distinct title\n" +
                "from albums natural join tracks\n" +
                "where mediatypeid not in (select mediatypeid from media_types where name = 'MPEG audio file')");
        ExecutionResult a = SqlExecutionUtils.execute(connection, "select title\n" +
                "from albums natural join tracks\n" +
                "where mediatypeid not in (select mediatypeid from media_types where name = 'MPEG audio file')");
        ExecutionResult b = SqlExecutionUtils.execute(connection, "select distinct title\n" +
                "from albums natural join tracks\n" +
                "where mediatypeid not in (select mediatypeid from media_types where name = 'MPEG audio file')\n" +
                "order by title");
        ExecutionResult c = SqlExecutionUtils.execute(connection, " select distinct title \n" +
                "from albums natural join tracks \n" +
                "where mediatypeid not in (select mediatypeid from media_types where name = 'MPEG audio file') \n" +
                "order by albumid");
        RowComparator comparator = new RowComparator();
        HashMap<String, Object> resultA = new HashMap<>();
        assertFalse(comparator.compare(a, criterion, resultA));
        assertTrue(comparator.compare(b, criterion, new HashMap<>()));
        assertTrue(comparator.compare(c, criterion, new HashMap<>()));
    }
}