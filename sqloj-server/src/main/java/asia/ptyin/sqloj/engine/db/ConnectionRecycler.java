package asia.ptyin.sqloj.engine.db;


import asia.ptyin.sqloj.config.SqlOjConfigurationProperties;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

/**
 * ConnectionRecycler counts down the remaining time for connection in pool,
 * and close the unused connections.
 */
@Service
public class ConnectionRecycler
{
    private final VariableDbConnectionPool pool;
    private final Timer timer = new Timer();
    private final SqlOjConfigurationProperties.Engine engineProperties;

    @Autowired
    public ConnectionRecycler(VariableDbConnectionPool pool, SqlOjConfigurationProperties.Engine engineProperties)
    {
        this.pool = pool;
        this.engineProperties = engineProperties;
    }


    @PostConstruct
    void init()
    {
        timer.schedule(new TimerTask()
        {
            @SneakyThrows
            @Override
            public void run()
            {
                synchronized (pool.getContainer())
                {
                    var toCloseConnections = pool.getContainer().values()
                            .stream()
                            .flatMap(Collection::stream)
                            .filter(connection -> (System.currentTimeMillis() - connection.getTimestamp()) >= engineProperties.getConnectionMaxLiveDuration())
                            .collect(Collectors.toList());

                    for(var connection : toCloseConnections)
                    {
                        connection.close();
                        pool.removeConnection(connection);
                    }
                }
            }
        }, engineProperties.getConnectionRecyclePeriod(), engineProperties.getConnectionRecyclePeriod());
    }
}
