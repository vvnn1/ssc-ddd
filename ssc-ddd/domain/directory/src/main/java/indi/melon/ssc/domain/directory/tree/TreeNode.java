package indi.melon.ssc.domain.directory.tree;

import indi.melon.ssc.domain.directory.exception.IllegalTreeNodeException;
import indi.melon.ssc.domain.directory.exception.TreeNodeAlreadyExistException;
import indi.melon.ssc.domain.directory.exception.TreeNodeNotSupportException;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
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
@Entity
public class TreeNode {
    @EmbeddedId
    private NodeID id;
    private String name;
    private String type;
    @OneToMany
    private List<TreeNode> childNodeList;
    private NodeID parentId;
    private LocalDateTime createTime;
    private Boolean expandable;
    @Transient
    private Order order;

    public boolean add(TreeNode childNode){
        if (!isRootNode()) {
            throw new TreeNodeNotSupportException("node " + name + " is not root node, it should not add others.");
        }

        if (childNode.isRootNode()) {
            throw new IllegalTreeNodeException("node " + childNode.name + " is root node,it should not be added to others.");
        }

        TreeNode parentNode = findParentNode(childNode);
        if (parentNode == null){
            return false;
        }
        parentNode.checkSameChildNode(childNode);
        parentNode.beParent(childNode);
        return true;
    }

    public List<TreeNode> getChildNodeList() {
        if (order != null){
            this.childNodeList.sort(order.comparator);
        }
        return Collections.unmodifiableList(this.childNodeList);
    }

    public void setChildNodeList(List<TreeNode> childNodeList) {
        this.childNodeList = new ArrayList<>(childNodeList);
    }

    public void setOrder(Order order) {
        this.order = order;
        LinkedList<TreeNode> queue = new LinkedList<>(Collections.singleton(this));
        while (!queue.isEmpty()) {
            TreeNode treeNode = queue.remove();
            treeNode.order = order;
            if (treeNode.childNodeList != null){
                queue.addAll(treeNode.childNodeList);
            }
        }
    }

    private void beParent(TreeNode childNode) {
        if (!Boolean.TRUE.equals(expandable)){
            throw new TreeNodeNotSupportException("node " + name + " is not expandable, it can not add " + childNode.name + ".");
        }
        childNode.order = this.order;
        this.childNodeList.add(childNode);
    }

    private boolean isRootNode(){
        return parentId == null;
    }

    private  void checkSameChildNode(TreeNode childNode){
        for (TreeNode treeNode : childNodeList) {
            if (Objects.equals(treeNode, childNode)){
                if (Objects.equals(treeNode.name, childNode.name)){
                    throw new TreeNodeAlreadyExistException("node name " + childNode.name + " already exist.");
                }
            }
        }
    }

    private TreeNode findParentNode(TreeNode childNode){
        LinkedList<TreeNode> queue = new LinkedList<>(Collections.singleton(this));
        TreeNode parentNode = null;

        while (!queue.isEmpty()){
            TreeNode treeNode = queue.remove();

            if (Objects.equals(treeNode.id, childNode.id)) {
                throw new IllegalTreeNodeException("node id " + treeNode.id + " already exist.");
            }

            if (Objects.equals(treeNode.id, childNode.parentId)){
                parentNode = treeNode;
            }

            if (treeNode.childNodeList != null){
                queue.addAll(treeNode.childNodeList);
            }
        }

        return parentNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeNode treeNode = (TreeNode) o;
        return Objects.equals(name, treeNode.name) && Objects.equals(type, treeNode.type) && Objects.equals(parentId, treeNode.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, parentId);
    }
}
