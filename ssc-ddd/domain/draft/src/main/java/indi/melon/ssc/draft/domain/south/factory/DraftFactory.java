package indi.melon.ssc.draft.domain.south.factory;

import indi.melon.ssc.draft.domain.draft.Draft;
import indi.melon.ssc.draft.domain.template.Template;

/**
 * @author vvnn1
 * @since 2024/10/28 0:46
 */
public interface DraftFactory {
    Draft create(Template template, String name, String creator);
    Draft create(Draft fromDraft, String name, String creator);
}
