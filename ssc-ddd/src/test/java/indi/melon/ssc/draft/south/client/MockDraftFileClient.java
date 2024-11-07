package indi.melon.ssc.draft.south.client;

import indi.melon.ssc.draft.domain.draft.Directory;
import indi.melon.ssc.draft.domain.draft.Draft;
import indi.melon.ssc.draft.domain.south.client.DraftFileTreeClient;

/**
 * @author vvnn1
 * @since 2024/10/26 20:54
 */
public class MockDraftFileClient implements DraftFileTreeClient {
    private Draft draft;
    private Directory directory;

    @Override
    public void create(Directory directory, Draft draft) {
        this.draft = draft;
        this.directory = directory;
    }

    public Draft getDraft(){
        return draft;
    }

    public Directory getDirectory() {
        return directory;
    }
}
