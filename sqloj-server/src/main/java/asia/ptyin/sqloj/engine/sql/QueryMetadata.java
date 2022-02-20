package asia.ptyin.sqloj.engine.sql;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/***
 * Metadata for query result.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@RequiredArgsConstructor
public class QueryMetadata
{
    // The index of list is the column index,
    // which starts from 0 to columnCount - 1.
    @Getter
    final private List<ColumnMetadata> columnMetadataList;
    // The key is the column label, the value is index of columnMetadataList.
    @Getter
    final private Map<String, Integer> columnLabel2Index;

    public QueryMetadata(ResultSetMetaData resultSetMetaData) throws SQLException
    {
        int columnCount = resultSetMetaData.getColumnCount();
        columnMetadataList = IntStream.range(0, columnCount)
                .mapToObj(i -> new ColumnMetadata(resultSetMetaData, i))
                .toList();
        columnLabel2Index = columnMetadataList.stream()
                .collect(Collectors.toMap(ColumnMetadata::getColumnLabel, ColumnMetadata::getColumnIndex));
    }

    public int getColumnCount()
    {
        return columnMetadataList.size();
    }

    /**
     * Get column metadata by index.
     * @param columnIndex column index range from [0, {@link #getColumnCount()}].
     * @return The column metadata if the index in [0, {@link #getColumnCount()}], {@code null} otherwise.
     */
    public ColumnMetadata getColumnMetadata(int columnIndex)
    {
        return columnIndex < getColumnCount() ?
                columnMetadataList.get(columnIndex) :
                null;
    }

    /**
     * Get column metadata by label.
     * @param columnLabel column label.
     * @return The column metadata if {@code columnLabel} exists, {@code null} otherwise.
     */
    public ColumnMetadata getColumnMetadata(String columnLabel)
    {
        return columnLabel2Index.containsKey(columnLabel) ?
                columnMetadataList.get(columnLabel2Index.get(columnLabel)) :
                null;
    }

    /**
     * Get column index by label.
     * @param columnLabel column label.
     * @return The column index if {@code columnLabel} exists, {@code -1} otherwise.
     */
    public int getColumnIndex(String columnLabel)
    {
        return columnLabel2Index.getOrDefault(columnLabel, -1);
    }

    public Set<String> getColumnLabels()
    {
        return columnLabel2Index.keySet();
    }

    public static boolean equalsColumnLabel(Set<String> columnLabelA, Set<String> columnLabelB)
    {
        return columnLabelA.equals(columnLabelB);
    }

}
