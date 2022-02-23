package asia.ptyin.sqloj.engine.result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.Duration;

/***
 * Interface of all results that task can produce.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public interface Result
{
    default String serialize()
    {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String value = null;
        try
        {
            value = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        return value;
    }
    Duration getTime();
    void setTime(Duration time);
}
