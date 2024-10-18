package indi.melon.ssc.domain.directory.tree;

import indi.melon.ssc.domain.directory.tree.exception.IllegalSortException;

import java.util.*;
import java.util.function.Function;

/**
 * @author vvnn1
 * @since 2024/9/25 23:24
 */
public class Sort {
    protected Comparator<TreeNode> comparator;
    private final List<String> sortSequence;

    private Sort() {
        sortSequence = new ArrayList<>();
    }

    public static Sort orderBy(TreeNodeField treeNodeField, Order order) {
        Objects.requireNonNull(treeNodeField);
        Objects.requireNonNull(order);

        Sort sort = new Sort();
        return sort.and(treeNodeField, order);
    }

    public Sort and(TreeNodeField treeNodeField, Order order) {
        if (comparator == null){
            comparator = Comparator.comparing(treeNodeField.keyExtractor, order.keyComparator);
        } else {
            comparator = comparator.thenComparing(treeNodeField.keyExtractor, order.keyComparator);
        }

        sortSequence.add(
                serialize(treeNodeField, order)
        );
        return this;
    }

    public String serialize() {
        return String.join(".", sortSequence);
    }

    public static Sort deserialize(String str) {
        Objects.requireNonNull(str);

        String[] splits = str.split("\\.");
        Comparator<TreeNode> comparator = Arrays.stream(splits)
                .map(Sort::str2Comparator)
                .reduce(Comparator::thenComparing)
                .orElse(null);

        Sort sort = new Sort();
        sort.comparator = comparator;
        sort.sortSequence.addAll(Arrays.asList(splits));
        return sort;
    }

    private static Comparator<TreeNode> str2Comparator(String str) {
        char[] strAry = str.toCharArray();

        int i = strAry.length -1;
        for (; i >= 0; i--) {
            if (strAry[i] >= 'A' && strAry[i] <= 'Z') {
                break;
            }
        }

        String fieldName = str.substring(0, i);
        String orderName = str.substring(i, i+1).toLowerCase() + str.substring(i+1);

        TreeNodeField treeNodeField;
        try {
            treeNodeField = TreeNodeField.valueOf(fieldName);
        } catch (IllegalArgumentException e){
            throw new IllegalSortException("not found " + fieldName + " of " + TreeNode.class + ". only support " + Arrays.toString(TreeNodeField.values()));
        }

        Order order;

        try {
            order = Order.valueOf(orderName);
        } catch (IllegalArgumentException e){
            throw new IllegalSortException("order " + orderName + " is illegal. only support " + Arrays.toString(Order.values()));
        }

        return Comparator.comparing(treeNodeField.keyExtractor, order.keyComparator);
    }

    private String serialize(TreeNodeField treeNodeField, Order order) {
        String orderName = order.name();
        return treeNodeField.name() + orderName.substring(0, 1).toUpperCase() + orderName.substring(1);
    }

    @Override
    public String toString() {
        return serialize();
    }

    public enum TreeNodeField {
        name(TreeNode::getName),
        type(TreeNode::getType),
        createTime(TreeNode::getCreateTime);

        final Function<TreeNode, ?> keyExtractor;

        TreeNodeField(Function<TreeNode, ?> keyExtractor) {
            this.keyExtractor = keyExtractor;
        }
    }

    public enum Order {
        asc(Comparator.naturalOrder()),
        desc(Comparator.reverseOrder());

        final Comparator keyComparator;

        Order(Comparator keyComparator) {
            this.keyComparator = keyComparator;
        }
    }
}
