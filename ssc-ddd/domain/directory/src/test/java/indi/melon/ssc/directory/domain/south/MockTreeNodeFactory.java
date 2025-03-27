package indi.melon.ssc.directory.domain.south;

import indi.melon.ssc.directory.domain.south.factory.TreeNodeFactory;
import indi.melon.ssc.directory.domain.tree.NodeID;
import indi.melon.ssc.directory.domain.tree.TreeNode;

/**
 * @author wangmenglong
 * @since 2025/3/26 17:18
 */
public class MockTreeNodeFactory implements TreeNodeFactory {

    private final String prefix;

    public MockTreeNodeFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public TreeNode create(String name, String type, boolean expandable, boolean isRoot) {
        return new TreeNode(
                new NodeID(prefix + name),
                name,
                type,
                expandable,
                isRoot
        );
    }
}
