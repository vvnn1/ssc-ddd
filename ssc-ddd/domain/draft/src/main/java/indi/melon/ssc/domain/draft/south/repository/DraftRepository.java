package indi.melon.ssc.domain.draft.south.repository;

import indi.melon.ssc.domain.draft.draft.DraftID;
import indi.melon.ssc.domain.draft.draft.Draft;

/**
 * @author vvnn1
 * @since 2024/10/4 23:31
 */
public interface DraftRepository {
    Draft draftOf(DraftID id);
    void save(Draft draft);
}
