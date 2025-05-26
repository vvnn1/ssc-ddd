package indi.melon.ssc.draft.domain.south.client;

import indi.melon.ssc.draft.domain.draft.Draft;

/**
 * @author vvnn1
 * @since 2024/10/26 20:54
 */
public class MockDraftFileClient implements DraftFileTreeClient {
    private Draft draft;
    private String directoryId;
    private String rootDirectoryId;

    @Override
    public void create(String directoryId, Draft draft) {
        this.draft = draft;
    }

    public Draft getDraft(){
        return draft;
    }

    public String getDirectoryId() {
        return directoryId;
    }

    public String getRootDirectoryId() {
        return rootDirectoryId;
    }
}
