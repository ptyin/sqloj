package asia.ptyin.sqloj.engine.comparator;

import asia.ptyin.sqloj.engine.sql.SqlExecutionUtils;
import lombok.extern.log4j.Log4j2;
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
        var criterion = SqlExecutionUtils.execute(connection, """
                select distinct title
                from albums natural join tracks
                where mediatypeid not in (select mediatypeid from media_types where name = 'MPEG audio file')
                """);
        var a = SqlExecutionUtils.execute(connection, """
                select title
                from albums natural join tracks
                where mediatypeid not in (select mediatypeid from media_types where name = 'MPEG audio file')
                """);
        var b = SqlExecutionUtils.execute(connection, """
                select distinct title
                from albums natural join tracks
                where mediatypeid not in (select mediatypeid from media_types where name = 'MPEG audio file')
                order by title
                """);
        var c = SqlExecutionUtils.execute(connection, """
                select distinct title
                from albums natural join tracks
                where mediatypeid not in (select mediatypeid from media_types where name = 'MPEG audio file')
                order by albumid
                """);
        var comparator = new RowComparator();
        var resultA = new HashMap<String, Object>();
        assertFalse(comparator.compare(a, criterion, resultA));
        assertTrue(comparator.compare(b, criterion, new HashMap<>()));
        assertTrue(comparator.compare(c, criterion, new HashMap<>()));
    }
}