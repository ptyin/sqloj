package asia.ptyin.sqloj.engine.task.impl;

import asia.ptyin.sqloj.engine.result.Result;
import asia.ptyin.sqloj.engine.task.Task;

import java.util.UUID;

/***
 * A task class to create .db file by SQL script.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public class CreateDatabaseTask extends Task<Result>
{
    public CreateDatabaseTask()
    {

    }

    @Override
    protected Result run() throws InterruptedException
    {
        return null;
    }
}
