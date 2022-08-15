package net.ptyin.sqloj.engine.result;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import java.sql.ResultSetMetaData;

/***
 * Column metadata records.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@NoArgsConstructor
@Getter
@Setter
public class ColumnMetadata
{
    private int columnIndex;

    private String catalogName;
    private int columnDisplaySize;
    private String columnLabel;
    @Getter(onMethod_ = @Deprecated)
    private String columnName;  // same as columnLabel due to the implementation of sqlite jdbc.
    private String columnTypeName;

    private int precision;
    private boolean autoIncrement;
    private boolean caseSensitive;
    /**
     * the nullability status of the given column;
     * one of columnNoNulls, columnNullable or columnNullableUnknown
     */
    private int nullable;
    private boolean readOnly;
    private boolean searchable;
    private boolean signed;
    private boolean writable;

    @SneakyThrows
    public ColumnMetadata(ResultSetMetaData resultSetMetaData, int column)
    {
        columnIndex = column++;  // columnIndex is zero-started.

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
