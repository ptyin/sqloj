package net.ptyin.sqloj.engine.comparators;

import net.ptyin.sqloj.engine.result.QueryResult;

import java.util.Map;
import java.util.Set;

/***
 * Compare the column labels including comparison between
 * the size of column labels and values.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public class ColumnLabelComparator implements LastQueryResultComparator
{

    @Override
    public boolean compare(QueryResult x, QueryResult criterion, Map<String, Object> comments)
    {
        Set<String> labelSetX = x.getMetadata().getColumnLabels();
        Set<String> labelSetCriterion = criterion.getMetadata().getColumnLabels();
        // Size matches and x contains criterion and criterion contains x.
        boolean match = labelSetX.size() == labelSetCriterion.size() &&
                labelSetX.containsAll(labelSetCriterion) &&
                labelSetCriterion.containsAll(labelSetX);
        if (!match)
            comments.put("column label", String.format("submitted: %s, criterion: %s", labelSetX, labelSetCriterion));
        return match;
    }
}
