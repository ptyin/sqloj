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
    private final Connection connection;
    private final long timestamp;

    public static VariableDbConnection create(Database database) throws SQLException
    {
        var connection = DriverManager.getConnection(database.getUrl());
        return new VariableDbConnection(database, connection, System.currentTimeMillis());
    }

    public void close() throws SQLException
    {
        connection.close();
    }
}
