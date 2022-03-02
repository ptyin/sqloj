package asia.ptyin.sqloj.engine.task;

import asia.ptyin.sqloj.engine.result.Result;
import asia.ptyin.sqloj.engine.task.service.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import java.time.Duration;


/***
 *
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Log4j2
@SpringBootTest
class TaskRunnerTest
{
    @Getter @Setter
    static class TestResult implements Result
    {
        private Duration time = Duration.ZERO;
        private boolean success = false;
    }

    @Autowired
    TaskRunner taskRunner;
    @Autowired
    TaskService taskService;

    final Object lock = new Object();

    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    @Test
    void submit() throws InterruptedException, JsonProcessingException
    {
        Assertions.assertNotNull(taskRunner);
        Task<Result> task = new Task<>()
        {
            @Override
            protected Result run() throws InterruptedException
            {
                synchronized (lock)
                {
                    log.debug("doing something");
                    Thread.sleep(1000);
                    log.debug("done");
                    lock.notify();
                }

                return new TestResult();
            }
        };
        taskRunner.submit(task);
        synchronized (lock)
        {
            lock.wait();
        }
        Thread.sleep(1000);
        var entity = taskService.getTask(task.getUuid());
        var deserializedValue = TaskService.getResult(entity.getResultJson(), TestResult.class);
        Assertions.assertEquals(deserializedValue.getTime(), Duration.ZERO);
        Assertions.assertFalse(deserializedValue.isSuccess());
        taskService.deleteTask(task.getUuid());
        Assertions.assertNull(taskService.getTask(task.getUuid()));
    }
}