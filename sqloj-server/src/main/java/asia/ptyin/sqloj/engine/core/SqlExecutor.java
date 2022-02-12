package asia.ptyin.sqloj.engine.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class SqlExecutor
{
    final JdbcTemplate jdbcTemplate;

    @Autowired
    public SqlExecutor(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String execute(String sql)
    {
//        jdbcTemplate.
        return null;
    }
}
