package indi.melon.ssc.draft.domain.south.repository;

import indi.melon.ssc.draft.domain.draft.Draft;
import indi.melon.ssc.draft.domain.draft.DraftID;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vvnn1
 * @since 2024/10/26 20:54
 */
public class MockDraftRepository implements DraftRepository{
    private final Map<DraftID, Draft> db = new HashMap<>();
    @Override
    public Draft draftOf(DraftID id) {
        return db.get(id);
    }

    @Override
    public void save(Draft draft) {
        db.put(draft.getId(), draft);
    }

    @Override
    public void delete(DraftID id) {
        db.remove(id);
    }
}
