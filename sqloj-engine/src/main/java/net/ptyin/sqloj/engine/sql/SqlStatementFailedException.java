package net.ptyin.sqloj.engine.sql;

/***
 * Thrown if SQL statements run failed.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public class SqlStatementFailedException extends RuntimeException
{
    public SqlStatementFailedException(String stmt, int stmtNumber, String source, Throwable cause)
    {
        super(String.format("Failed to execute SQL script statement #%s: %s \n origin source: `%s`",
                stmtNumber, stmt, source), cause);
    }
}
