package indi.melon.ssc.directory.north.local.message;


import indi.melon.ssc.common.exception.ApplicationValidationException;
import indi.melon.ssc.domain.common.utils.StringUtils;
import jakarta.annotation.Nonnull;

/**
 * @author vvnn1
 * @since 2024/10/5 12:51
 */
public record CreateNodeCommand(@Nonnull String name,
                                String type,
                                boolean expandable,
                                boolean isRoot,
                                String parentId) {
    public CreateNodeCommand {
        if (isRoot && !StringUtils.isBlank(parentId)){
            throw new ApplicationValidationException("can create a root node with parent node.");
        }

        if (!isRoot && StringUtils.isBlank(parentId)){
            throw new ApplicationValidationException("please assign parentId");
        }
    }

    public Boolean isCreateRootNode() {
        return isRoot;
    }
}

