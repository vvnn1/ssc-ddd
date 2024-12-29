package indi.melon.ssc.directory.domain.south.factory;

import indi.melon.ssc.directory.domain.tree.TreeNode;

/**
 * @author vvnn1
 * @since 2024/10/5 22:20
 */
public interface TreeNodeFactory {
    TreeNode create(String name, String type, boolean expandable, boolean isRoot);
}
