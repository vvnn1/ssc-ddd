package indi.melon.ssc.directory.south.repository;

import indi.melon.ssc.directory.domain.south.repository.TreeNodeRepository;
import indi.melon.ssc.directory.domain.tree.NodeID;
import indi.melon.ssc.directory.domain.tree.Sort;
import indi.melon.ssc.directory.domain.tree.TreeNode;
import indi.melon.ssc.directory.south.repository.dao.TreeNodeDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import static indi.melon.ssc.directory.domain.tree.Sort.Order.asc;
import static indi.melon.ssc.directory.domain.tree.Sort.Order.desc;
import static indi.melon.ssc.directory.domain.tree.Sort.TreeNodeField.createTime;
import static indi.melon.ssc.directory.domain.tree.Sort.TreeNodeField.name;
import static indi.melon.ssc.directory.domain.tree.TreeNodeUtil.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * @author vvnn1
 * @since 2024/10/7 22:11
 */
@DataJpaTest(
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {TreeNodeRepository.class, TreeNodeDao.class}
                )
        }
)
class TreeNodeRepositoryIT {
    @Autowired
    private TreeNodeRepository treeNodeRepository;

    @Test
    public void should_save_and_get_tree_node_normally() {
        TreeNode rootNode = buildRootNode(new NodeID("0"));
        TreeNode node1 = buildNode(new NodeID("1"));
        TreeNode node3 = buildNode(new NodeID("3"));
        TreeNode node4 = buildNode(new NodeID("4"));
        TreeNode node6 = buildUnExpandableNode(new NodeID("6"));

        rootNode.add(node1);
        rootNode.add(node3);
        node3.add(node4);
        node1.add(node6);

        rootNode.sortBy(Sort.orderBy(name, asc).and(createTime, desc));
        treeNodeRepository.save(rootNode);


        TreeNode treeNodeDB = treeNodeRepository.treeNodeOf(new NodeID("0"));

        TreeNode treeNode3 = treeNodeDB.get(new NodeID("3"));
        assertEquals(new NodeID("3"), treeNode3.getId());
        assertEquals("treeNode-" + new NodeID("3"), treeNode3.getName());
        assertEquals("directory", treeNode3.getType());
        assertEquals(rootNode.getId(), treeNode3.getParentNode().getId());
        assertTrue(treeNode3.getExpandable());
        assertFalse(treeNode3.getLocked());
        assertNull(treeNode3.getSort());
        assertEquals(1, treeNode3.getChildNodeList().size());


        TreeNode treeNode6 = treeNodeDB.get(new NodeID("6"));
        assertEquals(new NodeID("6"), treeNode6.getId());
        assertEquals("treeNode-" + new NodeID("6"), treeNode6.getName());
        assertEquals("directory", treeNode6.getType());
        assertEquals(node1.getId(), treeNode6.getParentNode().getId());
        assertFalse(treeNode6.getExpandable());
        assertFalse(treeNode6.getLocked());
        assertNull(treeNode6.getSort());
        assertTrue(treeNode6.getChildNodeList() == null || treeNode6.getChildNodeList().isEmpty());

        assertEquals(new NodeID("0"), treeNodeDB.getId());
        assertEquals("treeNode-" + new NodeID("0"), treeNodeDB.getName());
        assertEquals("directory", treeNodeDB.getType());
        assertNull(treeNodeDB.getParentNode());
        assertTrue(treeNodeDB.getExpandable());
        assertFalse(treeNodeDB.getLocked());
        assertEquals("nameAsc.createTimeDesc", treeNodeDB.getSort().serialize());
        assertEquals(2, treeNodeDB.getChildNodeList().size());

        assertArrayEquals(new NodeID[]{new NodeID("1"), new NodeID("3")}, rootNode.getChildNodeList().stream().map(TreeNode::getId).toArray());
    }

    @Test
    public void should_delete_tree_node_normally() {
        TreeNode rootNode = buildRootNode(new NodeID("0"));
        treeNodeRepository.save(rootNode);

        assertNotNull(treeNodeRepository.treeNodeOf(new NodeID("0")));
        treeNodeRepository.delete(new NodeID("0"));
        assertNull(treeNodeRepository.treeNodeOf(new NodeID("0")));
    }

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void should_delete_tree_node_cascade() {
        TreeNode rootNode = buildRootNode(new NodeID("0"));
        TreeNode node1 = buildNode(new NodeID("1"));
        TreeNode node2 = buildNode(new NodeID("2"));
        TreeNode node3 = buildNode(new NodeID("3"));

        rootNode.add(node1);
        rootNode.add(node2);
        node1.add(node3);

        rootNode = treeNodeRepository.save(rootNode);
        entityManager.flush();

        assertNotNull(treeNodeRepository.treeNodeOf(new NodeID("3")));

        node1 = rootNode.get(new NodeID("1"));
        rootNode.remove(node1);
        treeNodeRepository.save(rootNode);

        entityManager.flush();

        rootNode = treeNodeRepository.treeNodeOf(new NodeID("0"));


        assertNull(treeNodeRepository.treeNodeOf(new NodeID("3")));

    }
}