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

- Judge Condition
  - Default
  - Order sensitive
  - Metadata sensitive
- Judge Source
  - Query Output
  - Table A
  - View A
  - User Defined Source