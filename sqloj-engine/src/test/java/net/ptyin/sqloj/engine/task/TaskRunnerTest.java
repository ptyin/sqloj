package net.ptyin.sqloj.engine.task;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ptyin.sqloj.engine.result.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;


/***
 *
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Log4j2
class TaskRunnerTest
{
    @Getter @Setter
    static class TestResult implements Result
    {
        private Duration time = Duration.ZERO;
        private boolean success = false;
    }

    @Test
    void testInterrupt()
    {
        Assertions.assertFalse(Thread.currentThread().isInterrupted());
        try
        {
            Thread.currentThread().interrupt();
            Assertions.assertTrue(Thread.currentThread().isInterrupted());
            throw new InterruptedException("test");
        } catch (InterruptedException e)
        {
            Assertions.assertTrue(Thread.currentThread().isInterrupted());
        }
    }
}