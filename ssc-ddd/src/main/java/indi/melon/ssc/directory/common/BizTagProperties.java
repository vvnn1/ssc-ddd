package indi.melon.ssc.directory.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author wangmenglong
 * @since 2024/10/18 15:00
 */
@Component
public class BizTagProperties {
    public static String TREE_NODE_BIZ_TAG;
    public static String DRAFT_BIZ_TAG;

    @Value("${ssc.biz_tag.tree_node:tree_node_uuid}")
    public void setTreeNodeBizTag(String treeNodeBizTag) {
        TREE_NODE_BIZ_TAG = treeNodeBizTag;
    }

    @Value("${ssc.biz_tag.draft:draft_uuid}")
    public void setDraftBizTag(String draftBizTag) {
        DRAFT_BIZ_TAG = draftBizTag;
    }
}
