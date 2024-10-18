package indi.melon.ssc.directory.south.repository.dao;

import indi.melon.ssc.domain.directory.tree.TreeNode;
import indi.melon.ssc.domain.directory.tree.NodeID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author vvnn1
 * @since 2024/10/7 12:13
 */
public interface TreeNodeDao extends JpaRepository<TreeNode, NodeID> {

}
