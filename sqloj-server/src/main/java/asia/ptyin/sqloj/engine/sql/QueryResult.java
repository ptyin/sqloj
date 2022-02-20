package asia.ptyin.sqloj.engine.sql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class QueryResult implements Serializable
{
    /**
     * The first index of rows indicates row index,
     * while the second index indicates the index of that row.
     */
    private List<? extends List<Object>> rows;
    /**
     * Column metadata for result.
     */
    private QueryMetadata metadata;

    public QueryResult(ResultSet resultSet) throws SQLException
    {
        var resultSetMetaData = resultSet.getMetaData();
        metadata = new QueryMetadata(resultSetMetaData);
        int columnCount = metadata.getColumnCount();
        var list = new ArrayList<ArrayList<Object>>();
        while (resultSet.next())
        {
            var row = new ArrayList<>();
            for(int i = 1; i <= columnCount; i++)
                row.add(resultSet.getObject(i));
            list.add(row);
        }
        rows = list;
    }

    public String toJson() throws JsonProcessingException
    {
        var mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
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
}
