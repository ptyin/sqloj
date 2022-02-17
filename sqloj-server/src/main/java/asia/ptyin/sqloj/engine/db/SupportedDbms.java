package asia.ptyin.sqloj.engine.db;

public enum SupportedDbms
{
    POSTGRESQL("jdbc:postgresql://localhost:4001/"),
    MYSQL("jdbc:mysql://localhost:4002/"),
    SQLITE("jdbc:sqlite::resource:db/");

    private final String jdbcPrefix;
    SupportedDbms(String jdbcPrefix)
    {
        this.jdbcPrefix = jdbcPrefix;
    }
    public String getJdbcPrefix()
    {
        return jdbcPrefix;
    }
}
