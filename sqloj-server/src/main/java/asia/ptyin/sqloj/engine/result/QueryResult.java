package asia.ptyin.sqloj.engine.result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.*;


/**
 * In-memory stored query result, which not only contains the value retrieved
 * but the metadata (column type, nullable, writable, etc.) as well.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 * @see QueryMetadata Check the metadata info.
 */
@Getter
@Setter
@NoArgsConstructor
public class QueryResult implements Result
{
    /**
     * The first index of rows indicates row index,
     * while the second index indicates the index of that row.
     */
    private List<List<Object>> rows;
    /**
     * The list index indicates row index, the key of map indicates column label,
     * and the value of map indicates actual value.
     */
    private List<Map<String, Object>> labeledRows;
    /**
     * Column metadata for result.
     */
    private QueryMetadata metadata;

    @Getter(onMethod_ = @Override)
    private Duration time;

    public QueryResult(ResultSet resultSet) throws SQLException
    {
        var resultSetMetaData = resultSet.getMetaData();
        metadata = new QueryMetadata(resultSetMetaData);
        int columnCount = resultSetMetaData.getColumnCount();
        rows = new ArrayList<>();
        labeledRows = new ArrayList<>();
        while (resultSet.next())
        {
            var row = new ArrayList<>();
            var labeledRow = new HashMap<String, Object>();
            for(int i = 1; i <= columnCount; i++)
            {
                var value = resultSet.getObject(i);
                row.add(value);
                labeledRow.put(resultSetMetaData.getColumnLabel(i), value);
            }
            rows.add(row);
            labeledRows.add(labeledRow);
        }
    }

    @Override
    public String serialize()
    {
        var mapper = new ObjectMapper();
        String value = null;
        try
        {
            value = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        return value;
    }

    public static boolean equalsRow(List<Object> rowA, List<Object> rowB)
    {
        if(rowA.size() != rowB.size())
            return false;
        for(int i = 0; i < rowA.size(); i++)
            if(!rowA.get(i).equals(rowB.get(i)))
                return false;
        return true;
    }

    public static boolean equalsRow(List<Object> rowA, List<Object> rowB,
                                    QueryMetadata metadataA, QueryMetadata metadataB,
                                    String columnLabel)
    {
        int indexA = metadataA.getColumnIndex(columnLabel), indexB = metadataB.getColumnIndex(columnLabel);
        // either a or b does not have corresponding columnLabel.
        if(indexA == -1 || indexB == -1)
            return false;
        // assert indexA < rowA.size() && indexB < rowB.size();
        return rowA.get(indexA).equals(rowB.get(indexB));
    }

    /**
     * Convert the list of rows to a map from row to duplicate times.
     * @param rows The list of every row.
     * @return A map of which the key is row and the value is duplicate times.
     */
    public static <R> Map<R, Integer> rows2map(List<R> rows)
    {
        var map = new HashMap<R, Integer>();
        for (var row : rows)
        {
            if (map.containsKey(row))
                map.put(row, map.get(row) + 1);
            else
                map.put(row, 1);
        }
        return map;
    }
}
