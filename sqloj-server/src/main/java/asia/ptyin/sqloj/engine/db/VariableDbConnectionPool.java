package asia.ptyin.sqloj.engine.db;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

/**
 * Thread safe cached database connection pool for variable database.
 * Note that it should be registered as a singleton bean in the context.
 * @see VariableDbConnection
 */
public class VariableDbConnectionPool
{
    /**
     * The key indicates the database uuid, and the value set maintain database connections.
     */
    @Getter
    private final Map<UUID, VariableDbConnection> container = new HashMap<>();

    /**
     * Get a connection from the pool. if no matched connection found in pool, create one.
     * @param database The database you would like to connect.
     * @return Return a database connection corresponded with arguments.
     * @throws SQLException If a database access error occurs or the url is null.
     */
    public VariableDbConnection getConnection(Database database) throws SQLException
    {
        synchronized (container)
        {
            var uuid = database.getUuid();
            // If pool has corresponding database connection
            // and the connection is valid,
            // then return a pooled connection.
            if(container.containsKey(uuid) && container.get(uuid).getConnection().isValid(10))
                return container.get(uuid);
            // Otherwise, create a new connection.
            else
            {
                var variableDbConnection = VariableDbConnection.create(database);
                container.put(database.getUuid(), variableDbConnection);
                return variableDbConnection;
            }
        }
    }

    public void closeConnection(VariableDbConnection connection) throws SQLException
    {
        synchronized (container)
        {
            container.remove(connection.getDatabase().getUuid());
            connection.getConnection().close();
        }
    }

    public void closeConnection(UUID uuid) throws SQLException
    {
        synchronized (container)
        {
            if(container.containsKey(uuid))
               container.get(uuid).getConnection().close();
            container.remove(uuid);
        }
    }
}
