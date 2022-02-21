package asia.ptyin.sqloj.engine.task;

import asia.ptyin.sqloj.engine.result.Result;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/***
 * Result converter to store in database.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */

@Converter
public class TaskResultConverter implements AttributeConverter<Result, String>
{
    @Override
    public String convertToDatabaseColumn(Result result)
    {
        return null;
    }

    @Override
    public Result convertToEntityAttribute(String s)
    {
        return null;
    }
}
