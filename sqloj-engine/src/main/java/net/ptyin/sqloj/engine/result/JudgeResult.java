package net.ptyin.sqloj.engine.result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/***
 * Result of comparison.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Getter
@Setter
public class JudgeResult implements Result
{
    private Map<String, Object> comments = new HashMap<>();
    private Duration time;
    private boolean pass = false;
}
