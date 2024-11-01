package indi.melon.ssc.draft.domain.south.client;

import indi.melon.ssc.draft.domain.draft.Directory;
import indi.melon.ssc.draft.domain.draft.Draft;

/**
 * @author vvnn1
 * @since 2024/10/26 20:54
 */
public class MockDraftFileClient implements DraftFileTreeClient {
    private Draft draft;
    private String directoryId;

    @Override
    public void createFileNode(Directory directory, Draft draft) {
        this.draft = draft;
        this.directoryId = directoryId;
    }

    public Draft getDraft(){
        return draft;
    }

    public String getDirectoryId() {
        return directoryId;
    }
}
