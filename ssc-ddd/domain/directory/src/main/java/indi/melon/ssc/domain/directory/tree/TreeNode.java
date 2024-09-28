package indi.melon.ssc.domain.directory.tree;

import indi.melon.ssc.domain.directory.exception.IllegalNodeException;
import indi.melon.ssc.domain.directory.exception.NodeAlreadyExistException;
import indi.melon.ssc.domain.directory.exception.NodeNotSupportException;
import jakarta.persistence.*;
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
@Setter
@ToString
@Entity(name="TreeNode")
public class TreeNode {
    @EmbeddedId
    private NodeID id;
    private String name;
    private String type;
    @OneToMany
    @JoinColumn
    private List<TreeNode> childNodeList;
    private NodeID parentId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean expandable;
    private Boolean locked;
    @Transient
    private Order order;

    public boolean add(TreeNode childNode){
        if (!isRootNode()) {
            throw new NodeNotSupportException("node " + name + " is not root node, it should not add others.");
        }

        if (childNode.isRootNode()) {
            throw new IllegalNodeException("node " + childNode.name + " is root node,it should not be added to others.");
        }

        if (exist(childNode)) {
            throw new NodeAlreadyExistException("node " + childNode.name + " already exist.");
        }

        TreeNode parentNode = getTreeNode(childNode.parentId);
        if (parentNode == null){
            return false;
        }

        parentNode.allocate(childNode);
        return true;
    }

    public boolean remove(NodeID nodeID){
        if (!isRootNode()) {
            throw new NodeNotSupportException("node " + name + " is not root node, it should not delete others.");
        }

        TreeNode parentNode = findParentNode(nodeID);
        if (parentNode == null){
            return false;
        }

        return parentNode.deallocate(nodeID);
    }

    public boolean exist(TreeNode childNode){
        if (!isRootNode()) {
            throw new NodeNotSupportException("node " + name + " is not root node, it should not rename others.");
        }

        LinkedList<TreeNode> queue = new LinkedList<>(Collections.singleton(this));
        while (!queue.isEmpty()){
            TreeNode treeNode = queue.remove();

            if (treeNode.isSame(childNode)) {
                return true;
            }

            if (treeNode.childNodeList != null){
                treeNode.childNodeList.forEach(queue::push);
            }
        }
        return false;
    }

    public void rename(NodeID id, String name){
        if (!isRootNode()) {
            throw new NodeNotSupportException("node " + name + " is not root node, it should not rename others.");
        }

        TreeNode childNode = getTreeNode(id);
        if (childNode == null){
            throw new IllegalNodeException("node " + id + " is not exist.");
        }

        childNode.name = name;
        childNode.updateTime = LocalDateTime.now();
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



    private TreeNode getTreeNode(NodeID nodeID){
        LinkedList<TreeNode> queue = new LinkedList<>(Collections.singleton(this));
        while (!queue.isEmpty()){
            TreeNode treeNode = queue.remove();
            if (Objects.equals(treeNode.id, nodeID)) {
                return treeNode;
            }
            if (treeNode.childNodeList != null){
                queue.addAll(treeNode.childNodeList);
            }
        }
        return null;
    }

    private boolean deallocate(NodeID nodeID) {
        if (childNodeList == null){
            return false;
        }
        return childNodeList.removeIf(node -> Objects.equals(node.id, nodeID));
    }

    private void allocate(TreeNode childNode) {
        if (!Boolean.TRUE.equals(expandable)){
            throw new NodeNotSupportException("node " + name + " is not expandable, it can not add " + childNode.name + ".");
        }

        if (Boolean.TRUE.equals(locked)) {
            throw new NodeNotSupportException("node " + name + " is locked, it can not add " + childNode.name + ".");
        }

        childNodeList.add(childNode);
    }

    public List<TreeNode> getChildNodeList() {
        if (childNodeList == null){
            return null;
        }

        if (order == null){
            return Collections.unmodifiableList(childNodeList);
        }

        for (TreeNode treeNode : childNodeList) {
            treeNode.order = order;
        }
        childNodeList.sort(order.comparator);
        return Collections.unmodifiableList(childNodeList);
    }

    public void setChildNodeList(List<TreeNode> childNodeList) {
        if (childNodeList == null){
            this.childNodeList = null;
            return;
        }

        if (!expandable){
            throw new NodeNotSupportException("node " + name + " is not expandable, it should not add others.");
        }

        for (TreeNode childNode : childNodeList) {
            if (!Objects.equals(id, childNode.getParentId())) {
                throw new IllegalNodeException("node " + childNode.name + " is not child of node " + name + ".");
            }
        }
        this.childNodeList = new ArrayList<>(childNodeList);
    }

    private boolean isRootNode(){
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
}
