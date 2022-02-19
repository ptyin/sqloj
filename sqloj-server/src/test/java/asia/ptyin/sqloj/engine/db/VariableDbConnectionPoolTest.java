package asia.ptyin.sqloj.engine.db;

import asia.ptyin.sqloj.engine.sql.SqlExecutor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/***
 *
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Log4j2
class VariableDbConnectionPoolTest
{
    final static VariableDbConnectionPool pool = new VariableDbConnectionPool();
    final static UUID TEST_UUID = UUID.fromString("85bb0dba-5db0-43bc-b33e-6f2c1d5c4ed3");
    final static int THREAD_COUNT = 100;

    @BeforeAll
    static void setup()
    {
        // Creat several test database on MySQL DBMS
        try (var connection = DriverManager.getConnection("jdbc:sqlite:target/%s.db".formatted(TEST_UUID)))
        {
            SqlExecutor.execute(connection, """
                    create table if not exists test(test int);
                    """);
        } catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void cleanup() throws SQLException
    {
        pool.closeConnection(TEST_UUID);
        var file = new File("target/%s.db".formatted(TEST_UUID));
        if(!file.delete())
            log.error("Failed to delete %s.db.".formatted(TEST_UUID));
    }

    @Test
    void testThreadSafe() throws InterruptedException, SQLException
    {
        var service = Executors.newFixedThreadPool(THREAD_COUNT);
        var tasks = Collections.nCopies(THREAD_COUNT, (Callable<Object>) () ->
        {
            SqlExecutor.execute(pool.getConnection(new DatabaseImpl(TEST_UUID)).getConnection(),
                    """
                            insert into test values(0);
                            """);
            return null;
        });
        var start = System.currentTimeMillis();
        service.invokeAll(tasks);
        service.shutdown();
        if(!service.awaitTermination(60, TimeUnit.SECONDS))
            service.shutdownNow();
        if(!service.awaitTermination(60, TimeUnit.SECONDS))
            log.error("Thread pool did not terminate");
        long cost = System.currentTimeMillis() - start;
        log.info("Execution costs %d.%03ds.".formatted(cost / 1000, cost % 1000));

        Assertions.assertEquals(1, pool.getContainer().size());
        var connection = pool.getConnection(new DatabaseImpl(TEST_UUID)).getConnection();
        var statement = connection.createStatement();
        var results = statement.executeQuery("select count(*) as total from test");
        Assertions.assertTrue(results.next());
        Assertions.assertEquals(THREAD_COUNT, results.getInt("total"));
        statement.close();
    }

    /***
     * Default implementation for data interface {@link Database}.
     * @version 0.1.0
     * @author PTYin
     * @since 0.1.0
     */
    record DatabaseImpl(UUID uuid) implements Database
    {
        @Override
        public UUID getUuid()
        {
            return uuid;
        }

        @Override
        public String getName()
        {
            return uuid.toString();
        }

        @Override
        public String getUrl()
        {
            return "jdbc:sqlite:target/%s.db".formatted(uuid);
        }
    }


}