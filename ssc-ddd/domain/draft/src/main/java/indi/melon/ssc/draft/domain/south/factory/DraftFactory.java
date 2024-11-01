package indi.melon.ssc.draft.domain.south.factory;

import indi.melon.ssc.draft.domain.draft.Draft;
import indi.melon.ssc.draft.domain.template.Template;

/**
 * @author vvnn1
 * @since 2024/10/28 0:46
 */
public interface DraftFactory {
    Draft create(String name, Template template, String creator);
    Draft create(String name, Draft draft, String creator);
}
