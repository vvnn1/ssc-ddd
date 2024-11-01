package indi.melon.ssc.draft.domain.template;

import indi.melon.ssc.draft.domain.draft.DraftType;

/**
 * @author vvnn1
 * @since 2024/10/27 22:20
 */
public enum TemplateType {
    SQL;

    public DraftType draftType(){
        return DraftType.valueOf(name());
    }
}
