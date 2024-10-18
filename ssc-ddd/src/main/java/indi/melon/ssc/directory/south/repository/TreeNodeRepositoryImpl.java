package indi.melon.ssc.directory.south.repository;

import indi.melon.ssc.common.exception.ApplicationInfrastructureException;
import indi.melon.ssc.directory.south.repository.dao.TreeNodeDao;
import indi.melon.ssc.directory.domain.south.repository.TreeNodeRepository;
import indi.melon.ssc.directory.domain.tree.TreeNode;
import indi.melon.ssc.directory.domain.tree.NodeID;
import org.springframework.stereotype.Repository;

/**
 * @author vvnn1
 * @since 2024/10/6 23:54
 */
@Repository
public class TreeNodeRepositoryImpl implements TreeNodeRepository {
    private final TreeNodeDao treeNodeDao;

    public TreeNodeRepositoryImpl(TreeNodeDao treeNodeDao) {
        this.treeNodeDao = treeNodeDao;
    }

    @Override
    public TreeNode treeNodeOf(NodeID id) {
        try {
            return treeNodeDao.findById(id)
                    .orElse(null);
        } catch (Exception e) {
            throw new ApplicationInfrastructureException("find tree node by " + id + " fail.", e);
        }

    }

    @Override
    public void save(TreeNode treeNode) {
        try {
            treeNodeDao.save(treeNode);
        } catch (Exception e) {
            throw new ApplicationInfrastructureException("save tree node fail. node: " + treeNode, e);
        }
    }

    @Override
    public void remove(NodeID id) {
        try {
            treeNodeDao.deleteById(id);
        } catch (Exception e) {
            throw new ApplicationInfrastructureException("delete tree node " + id + " fail.", e);
        }
    }
}
