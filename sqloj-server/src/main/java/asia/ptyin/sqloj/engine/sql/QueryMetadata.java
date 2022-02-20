package asia.ptyin.sqloj.engine.sql;

import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;

/***
 * Metadata for query result.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Getter
public class QueryMetadata
{
    final private int columnCount;
    final private List<ColumnMetadata> columnMetadataList;

    public QueryMetadata(ResultSetMetaData resultSetMetaData) throws SQLException
    {
        columnCount = resultSetMetaData.getColumnCount();
        columnMetadataList = IntStream.range(1, columnCount + 1).mapToObj(i -> new ColumnMetadata(resultSetMetaData, i)).toList();
    }
}
