package asia.ptyin.sqloj.engine.impl;

public enum TaskType
{
    /**
     * Create a sqlite db file.
     */
    CREATE(0),
    /**
     * Execute SQL statements in an existed DB environment.
     */
    EXEC(1),
    /**
     * Judge SQL statements with specific rules.
     */
    JUDGE(2);

    /**
     * The smaller number means higher priority.
     */
    final int priority;
    TaskType(int priority)
    {
        this.priority = priority;
    }
}
