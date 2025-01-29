package indi.melon.ssc.draft.domain.draft;

import indi.melon.ssc.draft.domain.south.factory.DraftFactory;
import indi.melon.ssc.draft.domain.template.Template;


/**
 * @author vvnn1
 * @since 2024/10/26 19:16
 */
public class DraftManager {
    private final DraftFactory draftFactory;

    public DraftManager(DraftFactory draftFactory) {
        this.draftFactory = draftFactory;
    }

    public Draft create(Template template, EngineID engineId, String name, String creator) {
        Draft draft = draftFactory.create(
                template,
                name,
                creator
        );
        draft.assignEngine(engineId);
        return draft;
    }

    public Draft create(Draft fromDraft, String name, String creator) {
        Draft draft = draftFactory.create(
                fromDraft,
                name,
                creator
        );

        draft.assignConfiguration(fromDraft.getConfiguration());
        return draft;
    }
}
