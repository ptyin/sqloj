package asia.ptyin.sqloj.engine;

import asia.ptyin.sqloj.engine.sql.QueryResult;
import asia.ptyin.sqloj.engine.sql.SqlExecutionUtils;
import asia.ptyin.sqloj.engine.task.Task;

import java.sql.Connection;
import java.util.UUID;

/***
 * A task class to execute SQL script and return the query result.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public class ExecutionTask extends Task<QueryResult>
{

    private final Connection connection;
    private final String source;

    public ExecutionTask(UUID uuid, Connection connection, String source)
    {
        super(uuid);
        this.connection = connection;
        this.source = source;
    }

    @Override
    public QueryResult run() throws Exception
    {
        var queryResults = SqlExecutionUtils.execute(connection, source);
        return queryResults.get(queryResults.size() - 1);
    }
}
