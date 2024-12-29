package indi.melon.ssc.directory.domain.tree;

import indi.melon.ssc.directory.domain.tree.exception.IllegalNodeException;
import indi.melon.ssc.directory.domain.tree.exception.AlreadyExistException;
import indi.melon.ssc.directory.domain.tree.exception.NotFoundException;
import indi.melon.ssc.directory.domain.tree.exception.NotSupportException;
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
@ToString(exclude = "parentNode")
public class TreeNode {
    private NodeID id;
    private String name;
    private String type;
    private List<TreeNode> childNodeList;
    private TreeNode parentNode;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean expandable;
    private Boolean locked;
    @Getter(AccessLevel.NONE)
    private Boolean isRoot;
    private Sort sort;
    private String sourceId;

    public TreeNode() {
    }

    public TreeNode(NodeID id, String name, String type, Boolean expandable, Boolean isRoot) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.createTime = LocalDateTime.now();
        this.updateTime =  LocalDateTime.now();
        this.expandable = expandable;
        this.locked = false;
        this.isRoot = isRoot;
    }

    public boolean add(TreeNode childNode) {
        if (childNode.isRootNode()) {
            throw new IllegalNodeException("node " + childNode.name + " is root node,it should not be added to others.");
        }

        if (!isSupportModifyChildList()) {
            throw new NotSupportException("node " + childNode.name + " is not support add child.");
        }

        if (childNode.parentNode == this){
            return true;
        }

        if (childNode.parentNode != null) {
            throw new NotSupportException("node " + childNode.name + " has parent node,it should not be added to others. please use moveTo.");
        }

        if (checkNodeConflict(childNode)) {
            throw new AlreadyExistException("node " + childNode.name + " already exist.");
        }

        return allocate(childNode);
    }

    public TreeNode get(NodeID id) {
        return findTreeNode(id);
    }

    public boolean remove(TreeNode childNode) {
        if (!isSupportModifyChildList()) {
            throw new NotSupportException("node " + this.id + " is not support remove child.");
        }

        return deallocate(childNode);
    }

    public boolean checkNodeConflict(TreeNode childNode) {
        if (childNodeList == null) {
            return false;
        }

        for (TreeNode treeNode : childNodeList) {
            if (treeNode == childNode) {
                continue;
            }

            if (Objects.equals(childNode.name, treeNode.name)
                    && Objects.equals(childNode.type, treeNode.type)) {
                return true;
            }
        }

        return false;
    }

    public void rename(NodeID childId, String newName) {
        TreeNode childNode = get(childId);

        if (childNode == null) {
            throw new NotFoundException("node id:" + childId + " not found.");
        }

        if (!childNode.isSupportModify()) {
            throw new NotSupportException("node " + childNode.name + " is not support modify.");
        }

        boolean conflict = checkNodeConflict(new TreeNode(
                null,
                newName,
                childNode.type,
                childNode.expandable,
                childNode.isRoot
        ));
        if (conflict) {
            throw new AlreadyExistException("node " + childNode.name + "(" + childNode.type + ")"+ " already exist.");
        }

        childNode.rename(newName);
    }

    private void rename(String newName) {
        this.name = newName;
        this.updateTime = LocalDateTime.now();
    }

    public boolean isLocked() {
        return locked;
    }

    public void locked() {
        locked = true;
    }

    public void unlocked() {
        locked = false;
    }

    public boolean moveTo(TreeNode newParentNode){
        if (isRoot) {
            throw new NotSupportException("node " + name + " is root node,it can not be child.");
        }

        if (!newParentNode.isSupportModifyChildList()) {
            throw new NotSupportException("node " + newParentNode.name + " is locked/un-expandable. child node " + name + " can not add to it.");
        }

        if (parentNode == null) {
            throw new NotSupportException("node " + name + " has no parent. please use add.");
        }

        parentNode.deallocate(this);
        return newParentNode.allocate(this);
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

    private boolean deallocate(TreeNode childNode) {
        if (childNodeList == null) {
            return false;
        }
        childNode.parentNode = null;
        return childNodeList.remove(childNode);
    }

    private boolean allocate(TreeNode childNode) {
        if (childNodeList == null){
            childNodeList = new ArrayList<>();
        }

        childNode.parentNode = this;
        return childNodeList.add(childNode);
    }

    public List<TreeNode> getChildNodeList() {
        if (childNodeList == null) {
            return null;
        }

        if (sort == null) {
            return Collections.unmodifiableList(childNodeList);
        }

        childNodeList.sort(sort.comparator);
        return Collections.unmodifiableList(childNodeList);
    }

    boolean isRootNode() {
        return isRoot;
    }

    public void sortBy(Sort sort) {
        if (!expandable) {
            throw new NotSupportException("node " + name + " is not support sort.");
        }
        this.sort = sort;
    }

    public Boolean isRoot() {
        return isRoot;
    }

    private boolean isSupportModifyChildList() {
        return expandable && isSupportModify();
    }

    private boolean isSupportModify() {
        return !locked && (parentNode == null || parentNode.isSupportModify());
    }
}
