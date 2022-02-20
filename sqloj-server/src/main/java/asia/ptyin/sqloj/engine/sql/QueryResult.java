package asia.ptyin.sqloj.engine.sql;

import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class QueryResult
{
    /**
     * The first index of rows indicates row index, while the second index indicates the index of that row.
     */
    private final List<? extends List<Object>> rows;
    /**
     * Column metadata for result.
     */
    @Getter
    private final QueryMetadata metadata;

    public QueryResult(ResultSet resultSet) throws SQLException
    {
        var resultSetMetaData = resultSet.getMetaData();
        metadata = new QueryMetadata(resultSetMetaData);
        int columnCount = metadata.getColumnCount();
        var list = new ArrayList<ArrayList<Object>>();
        while (resultSet.next())
        {
            var row = new ArrayList<Object>();
            for(int i = 1; i <= columnCount; i++)
                row.add(resultSet.getObject(i));
            list.add(row);
        }
        rows = list;
    }

    public List<List<Object>> getRows()
    {
        return Collections.unmodifiableList(rows);
    }

    public static boolean equalsRow(List<Object> rowA, List<Object> rowB)
    {
        if(rowA.size() != rowB.size())
            return false;
        for(int i = 0; i < rowA.size(); i++)
            if(rowA.get(i) != rowB.get(i))
                return false;
        return true;
    }
}
