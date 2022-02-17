package asia.ptyin.sqloj.engine.db;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;


@Service
public class VariableDbConnectionPool
{
    /**
     * The key indicates the database uuid, and the value set maintain database connections.
     */
    private final Map<UUID, List<VariableDbConnection>> container = new HashMap<>();

    /**
     * Get a connection from the pool. if no matched connection found in pool, create one.
     * @param database The database you would like to connect.
     * @param username The user of the connection.
     * @param password Corresponding password for the username.
     * @return Return a database connection corresponded with arguments.
     * @throws SQLException If a database access error occurs or the url is null.
     */
    public VariableDbConnection getConnection(Database database, @Nullable String username, @Nullable String password) throws SQLException
    {
        synchronized (container)
        {
            var uuid = database.getUuid();
            // If pool does not have corresponding database connection,
            // then create a new connection.
            if(container.containsKey(uuid) && !container.get(uuid).isEmpty())
                return container.get(uuid).remove(container.get(uuid).size() - 1);
            else
            {
                if(!container.containsKey(uuid))
                    container.put(uuid, new LinkedList<>());
                return VariableDbConnection.create(database, username, password);
            }

        }
    }

    /**
     * Release connection and return it to the pool.
     */
    public void releaseConnection(VariableDbConnection connection)
    {
        synchronized (container)
        {
            var uuid = connection.getDatabase().getUuid();
            if(!container.containsKey(uuid))
                container.put(uuid, new LinkedList<>());
            container.get(uuid).add(connection);
        }
    }

    public void removeConnection(VariableDbConnection connection)
    {
        synchronized (container)
        {
            var uuid = connection.getDatabase().getUuid();
            assert container.get(uuid) != null;
            container.get(uuid).remove(connection);
        }
    }

    public Map<UUID, List<VariableDbConnection>> getContainer()
    {
        return container;
    }
}
