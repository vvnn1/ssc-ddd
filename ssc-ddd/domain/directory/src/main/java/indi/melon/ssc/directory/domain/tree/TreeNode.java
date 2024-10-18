package indi.melon.ssc.directory.domain.tree;

import indi.melon.ssc.directory.domain.tree.exception.IllegalNodeException;
import indi.melon.ssc.directory.domain.tree.exception.AlreadyExistException;
import indi.melon.ssc.directory.domain.tree.exception.NotFoundException;
import indi.melon.ssc.directory.domain.tree.exception.NotSupportException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.*;


/**
 * @author vvnn1
 * @since 2024/9/22 21:59
 */
@Getter
@Setter(AccessLevel.PACKAGE)
@ToString
@Entity
@Access(AccessType.FIELD)
public class TreeNode {
    @EmbeddedId
    private NodeID id;
    private String name;
    private String type;
    @OneToMany(orphanRemoval=true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "parentId")
    private List<TreeNode> childNodeList;

    @Embedded
    @AttributeOverride(
            name = "id", column = @Column(name="parentId", updatable = false, insertable = false)
    )
    private NodeID parentId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean expandable;
    private Boolean locked;
    @Convert(converter =  SortConverter.class)
    private Sort sort;

    public TreeNode() {
    }

    public TreeNode(NodeID id, String name, String type, List<TreeNode> childNodeList, NodeID parentId,  Boolean expandable) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.childNodeList = childNodeList;
        this.parentId = parentId;
        this.createTime = LocalDateTime.now();
        this.updateTime =  LocalDateTime.now();
        this.expandable = expandable;
        this.locked = false;
    }

    public boolean add(TreeNode childNode) {
        if (!isRootNode()) {
            throw new NotSupportException("node " + name + " is not root node, it should not add others.");
        }

        if (childNode.isRootNode()) {
            throw new IllegalNodeException("node " + childNode.name + " is root node,it should not be added to others.");
        }

        if (exist(childNode)) {
            throw new AlreadyExistException("node " + childNode.name + " already exist.");
        }

        TreeNode parentNode = findTreeNode(childNode.parentId);
        if (parentNode == null) {
            return false;
        }

        parentNode.allocate(childNode);
        return true;
    }

    public TreeNode get(NodeID id) {
        if (!isRootNode()){
            throw new IllegalNodeException("node " + this.id + " is not root node,it should not invoke get.");
        }
        return findTreeNode(id);
    }

    public boolean remove(NodeID nodeID) {
        if (!isRootNode()) {
            throw new NotSupportException("node " + name + " is not root node, it should not delete others.");
        }

        TreeNode treeNode = findTreeNode(nodeID);
        if (treeNode == null) {
            return false;
        }

        if (Boolean.TRUE.equals(treeNode.locked)) {
            throw new NotSupportException("node " + treeNode.name + " is locked, it can be remove.");
        }

        if (treeNode.isRootNode()) {
            throw new NotSupportException("node " + treeNode.name + " is root node, it can not be remove.");
        }

        TreeNode parentNode = findTreeNode(treeNode.parentId);
        if (parentNode == null) {
            return false;
        }

        return parentNode.deallocate(nodeID);
    }

    public boolean exist(TreeNode childNode) {
        if (!isRootNode()) {
            throw new NotSupportException("node " + name + " is not root node, it should not rename others.");
        }

        LinkedList<TreeNode> queue = new LinkedList<>(Collections.singleton(this));
        while (!queue.isEmpty()) {
            TreeNode treeNode = queue.remove();

            if (treeNode.isSame(childNode)) {
                return true;
            }

            if (treeNode.childNodeList != null) {
                treeNode.childNodeList.forEach(queue::push);
            }
        }
        return false;
    }

    public void rename(NodeID id, String name) {
        if (!isRootNode()) {
            throw new NotSupportException("node " + name + " is not root node, it should not rename others.");
        }

        TreeNode treeNode = findTreeNode(id);
        if (treeNode == null) {
            throw new IllegalNodeException("node " + id + " is not exist.");
        }

        if (treeNode.isRootNode()){
            throw new NotSupportException("root node " + id + " can not be rename.");
        }

        if (Boolean.TRUE.equals(treeNode.locked)) {
            throw new NotSupportException("node " + treeNode.name + " is locked, it can be rename.");
        }

        treeNode.name = name;
        treeNode.updateTime = LocalDateTime.now();
    }

    public void move(NodeID nodeID, NodeID toNodeID){
        if (!isRootNode()) {
            throw new NotSupportException("node " + name + " is not root node, it should not rename others.");
        }

        TreeNode node = findTreeNode(nodeID);
        if (node == null){
            throw new NotFoundException("node " + nodeID + "is not found.");
        }

        TreeNode oldParentNode = findTreeNode(node.parentId);
        if (oldParentNode == null) {
            throw new IllegalNodeException("node " + nodeID + " should specify a parent node, but its null.");
        }

        TreeNode newParentNode = findTreeNode(toNodeID);
        if (newParentNode == null) {
            throw new NotFoundException("node " + toNodeID + " is not found.");
        }
        newParentNode.allocate(node);
        oldParentNode.deallocate(nodeID);
    }

    public boolean isLocked(NodeID nodeID) {
        TreeNode treeNode = findTreeNode(nodeID);
        if (treeNode == null) {
            throw new NotFoundException("node " + nodeID + " is not found.");
        }
        return Boolean.TRUE.equals(treeNode.locked);
    }

    public void locked(NodeID nodeID) {
        TreeNode treeNode = findTreeNode(nodeID);
        if (treeNode == null) {
            throw new NotFoundException("node " + nodeID + " is not found.");
        }
        treeNode.locked = true;
    }

    public void unlocked(NodeID nodeID) {
        TreeNode treeNode = findTreeNode(nodeID);
        if (treeNode == null) {
            throw new NotFoundException("node " + nodeID + " is not found.");
        }
        treeNode.locked = false;
    }

    private TreeNode findParentNode(NodeID nodeID) {
        Deque<TreeNode> stack = new LinkedList<>(Collections.singleton(this));
        Deque<TreeNode> result = new LinkedList<>();
        while (!stack.isEmpty()) {
            TreeNode treeNode = stack.pop();
            if (result.peek() != null && !Objects.equals(treeNode.parentId, result.peek().id)) {
                result.pop();
            }

            if (Objects.equals(treeNode.id, nodeID)) {
                return result.peek();
            }

            if (treeNode.childNodeList == null || treeNode.childNodeList.isEmpty()) {
                continue;
            }

            result.push(treeNode);
            treeNode.childNodeList.forEach(stack::push);
        }

        return null;
    }


    private TreeNode findTreeNode(NodeID nodeID) {
        LinkedList<TreeNode> queue = new LinkedList<>(Collections.singleton(this));
        while (!queue.isEmpty()) {
            TreeNode treeNode = queue.remove();
            if (Objects.equals(treeNode.id, nodeID)) {
                return treeNode;
            }
            if (treeNode.childNodeList != null) {
                queue.addAll(treeNode.childNodeList);
            }
        }
        return null;
    }

    private boolean deallocate(NodeID nodeID) {
        if (childNodeList == null) {
            return false;
        }
        return childNodeList.removeIf(node -> Objects.equals(node.id, nodeID));
    }

    private void allocate(TreeNode childNode) {
        if (!Boolean.TRUE.equals(expandable)) {
            throw new NotSupportException("node " + name + " is not expandable, it can not add " + childNode.name + ".");
        }

        if (Boolean.TRUE.equals(locked)) {
            throw new NotSupportException("node " + name + " is locked, it can not add " + childNode.name + ".");
        }

        if (childNodeList == null){
            childNodeList = new ArrayList<>();
        }

        childNodeList.add(childNode);
        childNode.parentId = id;
    }

    public List<TreeNode> getChildNodeList() {
        if (childNodeList == null) {
            return null;
        }

        if (sort == null) {
            return Collections.unmodifiableList(childNodeList);
        }

        for (TreeNode treeNode : childNodeList) {
            if (treeNode.expandable){
                treeNode.sort = sort;
            }
        }
        childNodeList.sort(sort.comparator);
        return Collections.unmodifiableList(childNodeList);
    }

    public void setChildNodeList(List<TreeNode> childNodeList) {
        if (childNodeList == null) {
            this.childNodeList = null;
            return;
        }

        if (Boolean.FALSE.equals(expandable)) {
            throw new NotSupportException("node " + name + " is not expandable, it should not add others.");
        }

        for (TreeNode childNode : childNodeList) {
            if (!Objects.equals(id, childNode.getParentId())) {
                throw new IllegalNodeException("node " + childNode.name + " is not child of node " + name + ".");
            }
        }
        this.childNodeList = new ArrayList<>(childNodeList);
    }

    private boolean isRootNode() {
        return parentId == null;
    }

    private boolean isSame(TreeNode treeNode) {
        if (Objects.equals(id, treeNode.id)) {
            return true;
        }

        return Objects.equals(parentId, treeNode.parentId)
                && Objects.equals(name, treeNode.name)
                && Objects.equals(type, treeNode.type);
    }

    public void setSort(Sort sort) {
        if (!isRootNode()){
            throw new IllegalNodeException("only root node is allowed to sort. but " + name + " is a child.");
        }

        this.sort = sort;
    }

    @PreUpdate
    @PrePersist
    private void removeSortIfNotRoot() {
        if (!isRootNode()){
            this.sort = null;
        }
    }
}
