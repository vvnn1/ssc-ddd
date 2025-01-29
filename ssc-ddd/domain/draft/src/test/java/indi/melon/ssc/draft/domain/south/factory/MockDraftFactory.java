package indi.melon.ssc.draft.domain.south.factory;

import indi.melon.ssc.draft.domain.draft.Draft;
import indi.melon.ssc.draft.domain.draft.DraftID;
import indi.melon.ssc.draft.domain.template.Template;


/**
 * @author vvnn1
 * @since 2024/10/30 23:53
 */
public class MockDraftFactory implements DraftFactory {
    private final String prefix;

    public MockDraftFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Draft create(Template template, String name, String creator) {
        return new Draft(
                new DraftID(prefix + name),
                name,
                template,
                creator
        );
    }

    @Override
    public Draft create(Draft fromDraft, String name, String creator) {
        return new Draft(
                new DraftID(prefix + "copy_" + name),
                name,
                fromDraft,
                creator
        );
    }
}
