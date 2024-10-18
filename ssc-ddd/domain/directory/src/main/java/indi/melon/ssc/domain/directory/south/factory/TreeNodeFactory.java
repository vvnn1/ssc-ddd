package indi.melon.ssc.domain.directory.south.factory;

import indi.melon.ssc.domain.directory.tree.TreeNode;

/**
 * @author vvnn1
 * @since 2024/10/5 22:20
 */
public interface TreeNodeFactory {
    TreeNode create(String name, String type, Boolean expandable, String parentNodeId);
}
