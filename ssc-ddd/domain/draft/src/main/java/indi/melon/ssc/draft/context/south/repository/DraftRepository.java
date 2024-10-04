package indi.melon.ssc.draft.context.south.repository;

import indi.melon.ssc.draft.context.domain.draft.Draft;
import indi.melon.ssc.draft.context.domain.draft.DraftID;

/**
 * @author vvnn1
 * @since 2024/10/4 23:31
 */
public interface DraftRepository {
    Draft draftOf(DraftID id);
    void save(Draft draft);
}
