# TODO


- [ ] JUDGE STATEMENT
  - COMPARE CODE OUTPUT
  - COMPARE TABLE (VIEW) DATA
  - COMPARE TABLE (VIEW) METADATA
    - ```sql
      SELECT column_name,ordinal_position,data_type,column_type FROM
      (
      SELECT
      column_name,ordinal_position,
      data_type,column_type,COUNT(1) rowcount
      FROM information_schema.columns
      WHERE table_schema=DATABASE()
      AND table_name IN ('product_today','product_yesterday')
      GROUP BY
      column_name,ordinal_position,
      data_type,column_type
      HAVING COUNT(1)=1
      ) A;
      ```
  - COMPARE GRANTED PRIVILEGE
    - ```sql
      SELECT * FROM INFORMATION_SCHEMA.TABLE_PRIVILEGES WHERE GRANTEE = 'USERNAME'
      ```
      
- [ ] ~~使用二级队列保存任务~~
  - 第一级队列保存数据库名，优先队列实现，key是数据库对应的任务数量，value是二级队列
  - 第二级队列保存任务，采用FIFO队列实现
  - 当线程池未满时，从一级队列队首取出首个任务
  - 当线程池中任务完成时，存在两种情况
    - 若*已完成任务相同的数据库*任务队列不为空，p概率换入该数据库对应的任务队列首个任务（节省数据库连接开销），1-p概率换入一级队列队首的首个任务
    - 若*已完成任务相同的数据库*任务队列为空，则换入一级队列队首的首个任务
  - 为了防止饥饿，在一级队列额外添加一个队列，该队列永远排在队首，当任务等待轮次超过阈值时将该任务添加到该队列中

- [ ] Database connection caching