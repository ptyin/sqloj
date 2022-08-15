package net.ptyin.sqloj.engine.task.impl;

import net.ptyin.sqloj.engine.result.Result;
import net.ptyin.sqloj.engine.task.Task;

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
    protected Result invoke() throws InterruptedException
    {
        return null;
    }
}
