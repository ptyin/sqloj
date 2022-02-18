package asia.ptyin.sqloj.engine.db;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@SuppressWarnings("ClassCanBeRecord")
@RequiredArgsConstructor
@Getter
public class VariableDbConnection
{
    private final Database database;
    private final String username, password;
    private final Connection connection;
    private final long timestamp;

    public static VariableDbConnection create(Database database, String username, String password) throws SQLException
    {
        var url = database.getDbms().getJdbcPrefix() + database.getName();
        var connection = DriverManager.getConnection(url, username, password);
        return new VariableDbConnection(database, username, password, connection, System.currentTimeMillis());
    }

    public void close() throws SQLException
    {
        connection.close();
    }
}
