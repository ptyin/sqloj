package net.ptyin.sqloj.engine.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/***
 * SQL script execution result.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Getter @Setter
public class ExecutionResult implements Result
{
    private List<QueryResult> results = new ArrayList<>();
    private Duration time;

    @JsonIgnore
    public QueryResult getLastQueryResult()
    {
        return results.get(results.size() - 1);
    }

    public void add(QueryResult queryResult)
    {
        results.add(queryResult);
    }

    public QueryResult getQueryResult(int i)
    {
        return results.get(i);
    }
}
