package asia.ptyin.sqloj.engine.result;

import java.time.Duration;

/***
 * Interface of all results that task can produce.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public interface Result
{
    String serialize();
    Duration getTime();
}
