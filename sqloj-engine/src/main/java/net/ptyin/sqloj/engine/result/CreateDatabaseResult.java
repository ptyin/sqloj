package net.ptyin.sqloj.engine.result;

import java.time.Duration;

/***
 * Information of database, also the result of database creation.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public class CreateDatabaseResult implements Result
{
    @Override
    public String serialize()
    {
        return null;
    }

    @Override
    public Duration getTime()
    {
        return null;
    }

    @Override
    public void setTime(Duration time)
    {

    }
}
