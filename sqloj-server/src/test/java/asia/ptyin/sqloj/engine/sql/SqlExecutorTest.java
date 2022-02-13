package asia.ptyin.sqloj.engine.sql;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


class SqlExecutorTest
{
    String url = "db/chinook.db";

    @Test
    void dbExists()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("db/chinook.db").getFile());
        String absolutePath = file.getAbsolutePath();

        System.out.println(absolutePath);

        assertTrue(absolutePath.endsWith("/chinook.db"));
    }

    @Test
    void execute() throws SQLException
    {
        Connection conn = DriverManager.getConnection("jdbc:sqlite::resource:db/chinook.db");
//        var statement = conn.prepareStatement("delete from albums where AlbumId=1; delete from albums where AlbumId=2; insert into albums values (1, 'test', 1);");
        var statement = conn.createStatement();

        statement.executeBatch();
        var result = statement.executeQuery("select * from albums order by AlbumId");
        var metadata = result.getMetaData();
        int columnCount = metadata.getColumnCount();
        for (int i = 1; i <= columnCount; i++)
        {
            System.out.println(metadata.getColumnName(i) + ", ");
        }
        System.out.println();
        while (result.next()) {
            String row = "";
            for (int i = 1; i <= columnCount; i++) {
                row += result.getString(i) + ", ";
            }
            System.out.println();
            System.out.println(row);
        }
    }
}