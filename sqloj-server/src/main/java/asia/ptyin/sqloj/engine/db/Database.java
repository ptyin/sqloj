package asia.ptyin.sqloj.engine.db;

import lombok.Data;

import java.util.UUID;

@SuppressWarnings("ClassCanBeRecord")
@Data
public class Database
{
    private final UUID uuid;
    private final String name;
    private final SupportedDbms dbms;
}
