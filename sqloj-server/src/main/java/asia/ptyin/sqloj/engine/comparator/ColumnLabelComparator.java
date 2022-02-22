package asia.ptyin.sqloj.engine.comparator;

import asia.ptyin.sqloj.engine.result.QueryResult;

import java.util.Map;

/***
 * Compare the column labels including comparison between
 * the size of column labels and values.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
public class ColumnLabelComparator implements Comparator
{

    @Override
    public boolean compare(QueryResult x, QueryResult criterion, Map<String, Object> comments)
    {
        var labelSetX = x.getMetadata().getColumnLabels();
        var labelSetCriterion = criterion.getMetadata().getColumnLabels();
        // Size matches and x contains criterion and criterion contains x.
        boolean match = labelSetX.size() == labelSetCriterion.size() &&
                labelSetX.containsAll(labelSetCriterion) &&
                labelSetCriterion.containsAll(labelSetX);
        if (!match)
            comments.put("column label", "submitted:%s, criterion:%s".formatted(labelSetX, labelSetCriterion));
        return match;
    }
}
