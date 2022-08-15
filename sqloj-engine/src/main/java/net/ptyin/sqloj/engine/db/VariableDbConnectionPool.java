package net.ptyin.sqloj.engine.db;

import lombok.Getter;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
    private final ConcurrentHashMap<UUID, VariableDbConnection> container = new ConcurrentHashMap<>();

    /**
     * Get a connection from the pool. if no matched connection found in pool, create one.
     * @param database The database you would like to connect.
     * @return Return a database connection corresponded with arguments.
     * @throws SQLException If a database access error occurs or the url is null.
     */
    public VariableDbConnection getConnection(Database database) throws SQLException
    {
        UUID uuid = database.getUuid();
        // If pool has corresponding database connection
        // and the connection is valid,
        // then return a pooled connection.
        VariableDbConnection variableDbConnection = container.get(uuid);
        if(variableDbConnection != null && variableDbConnection.getConnection().isValid(10))
            return variableDbConnection;
        // Otherwise, create a new connection.
        else
        {
            variableDbConnection = VariableDbConnection.create(database);
            // If the uuid corresponding connection is not absent,
            // Close the newly created connection.
            VariableDbConnection present = container.putIfAbsent(uuid, variableDbConnection);
            if (present != null)
            {
                variableDbConnection.getConnection().close();
                return present;
            } else
                return variableDbConnection;
        }
    }

    /**
     * Close a specific connection in the pool.
     * @param uuid The uuid of Variable DB Connection that you would like to close.
     * @throws SQLException If a database access error occurs.
     */
    public void closeConnection(UUID uuid) throws SQLException
    {
        try
        {
            container.get(uuid).getConnection().close();
        } finally
        {
            container.remove(uuid);
        }
    }
}
