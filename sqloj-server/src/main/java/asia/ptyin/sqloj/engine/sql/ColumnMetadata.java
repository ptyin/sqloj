package asia.ptyin.sqloj.engine.sql;

import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/***
 * Column metadata records.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Getter
public class ColumnMetadata implements Serializable
{
    private final String catalogName;
    private final int columnDisplaySize;
    private final String columnLabel;
    private final String columnName;
    private final String columnTypeName;

    private final int precision;
    private final boolean autoIncrement;
    private final boolean caseSensitive;
    /**
     * the nullability status of the given column;
     * one of columnNoNulls, columnNullable or columnNullableUnknown
     */
    private final int nullable;
    private final boolean readOnly;
    private final boolean searchable;
    private final boolean signed;
    private final boolean writable;

    @SneakyThrows
    public ColumnMetadata(ResultSetMetaData resultSetMetaData, int column)
    {
        catalogName = resultSetMetaData.getCatalogName(column);
        columnDisplaySize = resultSetMetaData.getColumnDisplaySize(column);
        columnLabel = resultSetMetaData.getColumnLabel(column);
        columnName = resultSetMetaData.getColumnName(column);
        columnTypeName = resultSetMetaData.getColumnTypeName(column);

        precision = resultSetMetaData.getPrecision(column);
        autoIncrement = resultSetMetaData.isAutoIncrement(column);
        caseSensitive = resultSetMetaData.isCaseSensitive(column);
        nullable = resultSetMetaData.isNullable(column);
        readOnly = resultSetMetaData.isReadOnly(column);
        searchable = resultSetMetaData.isSearchable(column);
        signed = resultSetMetaData.isSigned(column);
        writable = resultSetMetaData.isWritable(column);
    }
}
