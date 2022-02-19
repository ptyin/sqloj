package asia.ptyin.sqloj.engine.db;

import java.util.UUID;


/**
 * Data interface for database.
 */
public interface Database
{
    UUID getUuid();
    String getName();
    String getUrl();
}
