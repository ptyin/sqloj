package asia.ptyin.sqloj.engine.sql;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


@Getter
public class QueryOutput
{
    /**
     * The key of HashMap columns indicates column name, while the value indicates the value of that column.
     */
    private TreeMap<String, List<Object>> columns;

    /**
     * The first index of rows indicates row index, while the second index indicates the index of that row.
     */
    private List<List<Object>> rows;

    public List<String> getColumnNames()
    {
        return columns.keySet().stream().toList();
    }

    public List<Object> getColumn(String columnName)
    {
        return columns.get(columnName);
    }

    public List<Object> getRow(int index)
    {
        return rows.get(index);
    }
}
