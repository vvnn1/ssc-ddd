package indi.melon.ssc.directory.south.repository;

import indi.melon.ssc.directory.domain.south.repository.TreeNodeRepository;
import indi.melon.ssc.directory.domain.tree.NodeID;
import indi.melon.ssc.directory.domain.tree.Sort;
import indi.melon.ssc.directory.domain.tree.TreeNode;
import indi.melon.ssc.directory.south.repository.dao.TreeNodeDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static indi.melon.ssc.directory.domain.tree.Sort.Order.asc;
import static indi.melon.ssc.directory.domain.tree.Sort.Order.desc;
import static indi.melon.ssc.directory.domain.tree.Sort.TreeNodeField.createTime;
import static indi.melon.ssc.directory.domain.tree.Sort.TreeNodeField.name;
import static indi.melon.ssc.directory.domain.tree.TreeNodeUtil.buildNode;
import static indi.melon.ssc.directory.domain.tree.TreeNodeUtil.buildUnExpandableNode;
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
    public void should_save_and_get_tree_node_normally(){
        TreeNode rootNode = buildNode(new NodeID("0"), null);
        Arrays.asList(
                buildNode(new NodeID("1"), new NodeID("0")),
                buildNode(new NodeID("3"), new NodeID("0")),
                buildNode(new NodeID("4"), new NodeID("3")),
                buildUnExpandableNode(new NodeID("6"), new NodeID("1"))
        ).forEach(rootNode::add);

        rootNode.setSort(Sort.orderBy(name, asc).and(createTime, desc));
        treeNodeRepository.save(rootNode);


        TreeNode treeNodeDB = treeNodeRepository.treeNodeOf(new NodeID("0"));

        TreeNode treeNode3 = treeNodeDB.get(new NodeID("3"));
        assertEquals(new NodeID("3"), treeNode3.getId());
        assertEquals("treeNode-" + new NodeID("3"), treeNode3.getName());
        assertEquals("directory", treeNode3.getType());
        assertEquals(new NodeID("0"), treeNode3.getParentId());
        assertTrue(treeNode3.getExpandable());
        assertFalse(treeNode3.getLocked());
        assertNull(treeNode3.getSort());
        assertEquals(1, treeNode3.getChildNodeList().size());


        TreeNode treeNode6 = treeNodeDB.get(new NodeID("6"));
        assertEquals(new NodeID("6"), treeNode6.getId());
        assertEquals("treeNode-" + new NodeID("6"), treeNode6.getName());
        assertEquals("directory", treeNode6.getType());
        assertEquals(new NodeID("1"), treeNode6.getParentId());
        assertFalse(treeNode6.getExpandable());
        assertFalse(treeNode6.getLocked());
        assertNull(treeNode6.getSort());
        assertEquals(0, treeNode6.getChildNodeList().size());

        assertEquals(new NodeID("0"), treeNodeDB.getId());
        assertEquals("treeNode-" + new NodeID("0"), treeNodeDB.getName());
        assertEquals("directory", treeNodeDB.getType());
        assertNull(treeNodeDB.getParentId());
        assertTrue(treeNodeDB.getExpandable());
        assertFalse(treeNodeDB.getLocked());
        assertEquals("nameAsc.createTimeDesc", treeNodeDB.getSort().serialize());
        assertEquals(2, treeNodeDB.getChildNodeList().size());

        assertArrayEquals(new NodeID[]{new NodeID("1"), new NodeID("3")}, rootNode.getChildNodeList().stream().map(TreeNode::getId).toArray());
    }

    @Test
    public void should_delete_tree_node_normally(){
        TreeNode rootNode = buildNode(new NodeID("0"), null);
        treeNodeRepository.save(rootNode);

        assertNotNull(treeNodeRepository.treeNodeOf(new NodeID("0")));
        treeNodeRepository.delete(new NodeID("0"));
        assertNull(treeNodeRepository.treeNodeOf(new NodeID("0")));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void should_delete_tree_node_cascade(){
        TreeNode rootNode = buildNode(new NodeID("0"), null);

        Arrays.asList(
                buildNode(new NodeID("1"), new NodeID("0")),
                buildNode(new NodeID("2"), new NodeID("0")),
                buildNode(new NodeID("3"), new NodeID("1"))
        ).forEach(rootNode::add);
        treeNodeRepository.save(rootNode);

        assertNotNull(treeNodeRepository.treeNodeOf(new NodeID("3")));

        rootNode = treeNodeRepository.treeNodeOf(new NodeID("0"));
        rootNode.remove(new NodeID("1"));
        treeNodeRepository.save(rootNode);

        assertNull(treeNodeRepository.treeNodeOf(new NodeID("3")));

    }
}