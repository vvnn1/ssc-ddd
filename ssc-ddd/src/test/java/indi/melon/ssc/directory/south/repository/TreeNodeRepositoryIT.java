package indi.melon.ssc.directory.south.repository;

import indi.melon.ssc.configuration.SscTestConfiguration;
import indi.melon.ssc.domain.directory.south.repository.TreeNodeRepository;
import indi.melon.ssc.domain.directory.tree.NodeID;
import indi.melon.ssc.domain.directory.tree.TreeNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static indi.melon.ssc.domain.directory.tree.TreeNodeUtil.buildNode;
import static indi.melon.ssc.domain.directory.tree.TreeNodeUtil.buildTree;


/**
 * @author vvnn1
 * @since 2024/10/7 22:11
 */
@SpringBootTest(classes = SscTestConfiguration.class)
class TreeNodeRepositoryIT {
    @Autowired
    private TreeNodeRepository treeNodeRepository;

    @Test
    public void test(){
        TreeNode treeNode = treeNodeRepository.treeNodeOf(new NodeID("0"));
        System.out.println(treeNode.getChildNodeList());
    }
}