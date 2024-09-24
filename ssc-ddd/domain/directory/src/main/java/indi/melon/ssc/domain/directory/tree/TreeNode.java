package indi.melon.ssc.domain.directory.tree;

import indi.melon.ssc.domain.directory.exception.IllegalTreeNodeException;
import indi.melon.ssc.domain.directory.exception.TreeNodeAlreadyExistException;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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

    @OneToMany
    private List<TreeNode> childNodeList;

    private NodeID parentId;

    private LocalDateTime createTime;

    public boolean add(TreeNode childNode){
        if (childNodeList == null) {
            return false;
        }

        if (childNode.parentId == null) {
            throw new IllegalTreeNodeException("node " + childNode.getName() + ", its parent id should not be null");
        }

        LinkedList<TreeNode> queue = new LinkedList<>(Collections.singleton(this));
        TreeNode parentNode = null;

        while (!queue.isEmpty()){
            TreeNode treeNode = queue.remove();

            if (Objects.equals(treeNode.getId(), childNode.getId())) {
                throw new TreeNodeAlreadyExistException("node id " + treeNode.getId() + " already exist.");
            }

            if (Objects.equals(treeNode.getName(), childNode.getName())){
                throw new TreeNodeAlreadyExistException("node name " + treeNode.getName() + " already exist.");
            }

            if (Objects.equals(treeNode.getId(), childNode.getParentId())){
                parentNode = treeNode;
            }

            queue.addAll(treeNode.getChildNodeList());
        }

        if (parentNode == null){
            return false;
        }

        parentNode.beParentOf(childNode);
        return true;
    }

    public List<TreeNode> getChildNodeList() {
        return Collections.unmodifiableList(this.childNodeList);
    }

    public void setChildNodeList(List<TreeNode> childNodeList) {
        this.childNodeList = new ArrayList<>(childNodeList);
        this.childNodeList.sort(this::byCreateTimeAsc);
    }

    private void beParentOf(TreeNode childNode) {
        this.childNodeList.add(childNode);
    }

    private int byCreateTimeAsc(TreeNode t1, TreeNode t2){
        return t1.createTime.compareTo(t2.createTime);
    }

}
