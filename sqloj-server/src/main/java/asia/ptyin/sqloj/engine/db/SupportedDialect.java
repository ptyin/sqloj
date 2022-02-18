package asia.ptyin.sqloj.engine.db;

public enum SupportedDialect
{
    POSTGRESQL("jdbc:postgresql://localhost:4001/"),
    MYSQL("jdbc:mysql://localhost:4002/"),
    SQLITE("jdbc:sqlite::resource:db/");

    private final String jdbcPrefix;
    SupportedDialect(String jdbcPrefix)
    {
        this.jdbcPrefix = jdbcPrefix;
    }
    public String getJdbcPrefix()
    {
        return jdbcPrefix;
    }
}
