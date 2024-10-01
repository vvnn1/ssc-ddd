package indi.melon.ssc.directory.context.domain.tree;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.function.Function;

/**
 * @author vvnn1
 * @since 2024/9/25 23:24
 */
public class Order {

    public static final java.util.Comparator<Comparable> asc = java.util.Comparator.naturalOrder();
    public static final java.util.Comparator<Comparable> desc = java.util.Comparator.reverseOrder();

    protected Comparator<TreeNode> comparator;

    public static final Function<TreeNode, String> name = TreeNode::getName;
    public static final Function<TreeNode, String> type = TreeNode::getType;
    public static final Function<TreeNode, LocalDateTime> createTime = TreeNode::getCreateTime;



    public static <U> Order orderBy(Function<? super TreeNode, ? extends U> keyExtractor,
                                    java.util.Comparator<? super U> keyComparator) {
        Order nodeComparator = new Order();
        nodeComparator.comparator = java.util.Comparator.comparing(keyExtractor, keyComparator);
        return nodeComparator;
    }

    public <U> Order and(Function<? super TreeNode, ? extends U> keyExtractor,
                         java.util.Comparator<? super U> keyComparator) {
        comparator = comparator.thenComparing(keyExtractor, keyComparator);
        return this;
    }
}
