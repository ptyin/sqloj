package asia.ptyin.sqloj.engine.db;

import asia.ptyin.sqloj.config.SqlOjConfigurationProperties;
import asia.ptyin.sqloj.engine.sql.SqlExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.PostConstruct;
import java.sql.DriverManager;

/***
 *
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@SpringBootTest
class VariableDbConnectionPoolTest
{
    @Autowired
    SqlOjConfigurationProperties.Engine engineProperties;

    @PostConstruct
    void setup()
    {
        // Creat several test database on MySQL DBMS
        try (var mysqlConnection = DriverManager.getConnection("jdbc:mysql://localhost:4002/",
                engineProperties.getMysqlDefaultUsername(), engineProperties.getMysqlDefaultPassword());
             var postgresqlConnection = DriverManager.getConnection("jdbc:postgresql://localhost:4001/",
                     engineProperties.getPostgresqlDefaultUsername(), engineProperties.getPostgresqlDefaultPassword()))
        {
            SqlExecutor.execute(mysqlConnection, """
                    create database if not exists test_mysql_0;
                    create database if not exists test_mysql_1;
                    create database if not exists test_mysql_2;
                    """);

            SqlExecutor.execute(postgresqlConnection, """
                    drop database if exists test_postgresql_0;
                    create database test_postgresql_0;
                    drop database if exists test_postgresql_1;
                    create database test_postgresql_1;
                    """);
        } catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void testThreadSafe()
    {

    }

}