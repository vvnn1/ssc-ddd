package indi.melon.ssc.draft.domain.south.repository;

import indi.melon.ssc.draft.domain.draft.DraftID;
import indi.melon.ssc.draft.domain.draft.Draft;

/**
 * @author vvnn1
 * @since 2024/10/4 23:31
 */
public interface DraftRepository {
    Draft draftOf(DraftID id);
    void save(Draft draft);
    void delete(DraftID id);
}
