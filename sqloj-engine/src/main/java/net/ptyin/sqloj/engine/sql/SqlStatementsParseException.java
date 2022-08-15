package net.ptyin.sqloj.engine.sql;

/***
 * Thrown if  SQL statements cannot be properly parsed.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public class SqlStatementsParseException extends RuntimeException
{
    public SqlStatementsParseException(String message, String source)
    {
        super(String.format("Failed to parse SQL statements: %s, \n origin source: `%s`", message, source));
    }
}
