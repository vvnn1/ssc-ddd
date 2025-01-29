package indi.melon.ssc.draft.domain.south.client;

import indi.melon.ssc.draft.domain.draft.Directory;
import indi.melon.ssc.draft.domain.draft.Draft;

/**
 * @author vvnn1
 * @since 2024/10/26 19:48
 */
public interface DraftFileTreeClient {
    void create(String directoryId, Draft draft);
}
