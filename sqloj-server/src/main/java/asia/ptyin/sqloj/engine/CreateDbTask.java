package asia.ptyin.sqloj.engine;

import asia.ptyin.sqloj.engine.result.Result;
import asia.ptyin.sqloj.engine.task.Task;

import java.util.UUID;

/***
 * A task class to create .db file by SQL script.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public class CreateDbTask extends Task<Result>
{
    public CreateDbTask(UUID uuid)
    {
        super(uuid);
    }

    @Override
    protected Result run() throws InterruptedException
    {
        return null;
    }
}
