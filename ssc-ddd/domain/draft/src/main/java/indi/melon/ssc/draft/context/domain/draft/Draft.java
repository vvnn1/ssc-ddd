package indi.melon.ssc.draft.context.domain.draft;

import indi.melon.ssc.draft.context.domain.version.Version;

import java.time.LocalDateTime;

/**
 * @author vvnn1
 * @since 2024/10/1 23:00
 */
public class Draft {
    private DraftID id;
    private String content;
    private DraftType type;
    private String creator;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;


    public void rollback(Version version) {

    }
}
