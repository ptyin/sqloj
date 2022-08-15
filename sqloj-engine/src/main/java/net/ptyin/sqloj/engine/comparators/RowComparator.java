package net.ptyin.sqloj.engine.comparators;

import net.ptyin.sqloj.engine.result.QueryResult;
import lombok.NonNull;

import java.util.Map;

import static net.ptyin.sqloj.engine.result.QueryResult.rows2map;

/***
 * Compare the query result regarding key-value.
 * Implicit comparator for all judgement.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 * @see Comparator
 */
public class RowComparator implements LastQueryResultComparator
{
    @Override
    public boolean compare(QueryResult x, QueryResult criterion, @NonNull Map<String, Object> comments)
    {
        Map<Map<String, Object>, Integer> mapX = rows2map(x.getLabeledRows());
        Map<Map<String, Object>, Integer> mapCriterion = rows2map(criterion.getLabeledRows());
        int redundantRows = 0, lackRows = 0;
        for (Map<String, Object> row : mapX.keySet())
        {
            // Result x has rows more than criterion.
            redundantRows += Math.max(0, mapX.get(row) - mapCriterion.getOrDefault(row, 0));

        }
        for (Map<String, Object> row : mapCriterion.keySet())
            // Result x has rows less than criterion.
            lackRows += Math.max(0, mapCriterion.get(row) - mapX.getOrDefault(row, 0));
        if(redundantRows > 0)
            comments.put("redundant rows", redundantRows);
        if(lackRows > 0)
            comments.put("lack rows", lackRows);
        return redundantRows + lackRows == 0;
    }

}
