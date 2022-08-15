package net.ptyin.sqloj.engine.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.ptyin.sqloj.engine.sql.SqlExecutionUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
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
        try (Connection connection = DriverManager.getConnection(String.format("jdbc:sqlite:target/%s.db", TEST_UUID)))
        {
            SqlExecutionUtils.execute(connection, "create table if not exists test(test int);");
        } catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void cleanup() throws SQLException
    {
        pool.closeConnection(TEST_UUID);
        File file = new File(String.format("target/%s.db", TEST_UUID));
        if(!file.delete())
            log.error(String.format("Failed to delete %s.db.", TEST_UUID));
    }

    @Test
    void testThreadSafe() throws InterruptedException, SQLException
    {
        ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Callable<Object>> tasks = Collections.nCopies(THREAD_COUNT, () ->
        {
            SqlExecutionUtils.execute(pool.getConnection(new DatabaseImpl(TEST_UUID)).getConnection(),
                    "insert into test values(0);");
            return null;
        });
        long start = System.currentTimeMillis();
        service.invokeAll(tasks);
        service.shutdown();
        if(!service.awaitTermination(60, TimeUnit.SECONDS))
            service.shutdownNow();
        if(!service.awaitTermination(60, TimeUnit.SECONDS))
            log.error("Thread pool did not terminate");
        long cost = System.currentTimeMillis() - start;
        log.info(String.format("Execution costs %d.%03ds.", cost / 1000, cost % 1000));

        Assertions.assertEquals(1, pool.getContainer().size());
        Connection connection = pool.getConnection(new DatabaseImpl(TEST_UUID)).getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("select count(*) as total from test");
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
    @RequiredArgsConstructor
    static class DatabaseImpl implements Database
    {
        private final UUID uuid;

        @Override
        public UUID getUuid()
        {
            return uuid;
        }

        @Override
        public String getUrl()
        {
            return String.format("jdbc:sqlite:target/%s.db", uuid);
        }
    }


}