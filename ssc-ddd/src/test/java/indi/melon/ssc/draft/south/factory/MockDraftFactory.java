package indi.melon.ssc.draft.south.factory;

import indi.melon.ssc.draft.domain.draft.Draft;
import indi.melon.ssc.draft.domain.draft.DraftID;
import indi.melon.ssc.draft.domain.south.factory.DraftFactory;
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
    public Draft create(String name, Template template, String creator) {
        return new Draft(
                new DraftID(prefix + name),
                name,
                template,
                creator
        );
    }

    @Override
    public Draft create(String name, Draft draft, String creator) {
        return new Draft(
                new DraftID(prefix + "copy_" + name),
                name,
                draft,
                creator
        );
    }
}
