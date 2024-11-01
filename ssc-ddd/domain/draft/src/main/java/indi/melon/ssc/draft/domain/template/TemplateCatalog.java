package indi.melon.ssc.draft.domain.template;

import indi.melon.ssc.draft.domain.draft.DraftCatalog;

/**
 * @author vvnn1
 * @since 2024/10/27 22:19
 */
public enum TemplateCatalog {
    STREAM,
    BATCH;

    public DraftCatalog draftCatalog(){
        return DraftCatalog.valueOf(name());
    }
}
