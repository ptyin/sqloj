package asia.ptyin.sqloj.engine.comparator;

import asia.ptyin.sqloj.engine.sql.SqlExecutionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class ColumnLabelComparatorTest
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
        Comparator comparator = new ColumnLabelComparator();

        var criterion = SqlExecutionUtils.execute(connection, """
                select title, albumid, artistid from albums;
                """).get(0);
        var a = SqlExecutionUtils.execute(connection, """
                select albumid, artistid, title from albums;
                """).get(0);
        var b = SqlExecutionUtils.execute(connection, """
                select artistid, title from albums;
                """).get(0);
        var c = SqlExecutionUtils.execute(connection, """
                select * from albums;
                """).get(0);
        assertTrue(comparator.compare(a, criterion, new HashMap<>()));
        var resultB = new HashMap<String, Object>();
        assertFalse(comparator.compare(b, criterion, resultB));
        assertTrue(resultB.containsKey("column label"));
        assertTrue(comparator.compare(c, criterion, new HashMap<>()));
        connection.rollback();
        connection.close();
    }
}