package indi.melon.ssc.draft.domain.draft;

import indi.melon.ssc.draft.domain.version.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author vvnn1
 * @since 2024/10/1 23:00
 */
@Getter
@Setter(AccessLevel.PACKAGE)
@ToString
public class Draft {
    private DraftID id;
    private String content;
    private DraftType type;
    private String creator;
    private String modifier;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Draft() {
    }

    public Draft(DraftID id, DraftType type, String creator) {
        this.id = id;
        this.type = type;
        this.creator = creator;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public void rollback(Version version, String modifier) {
        this.modifier = modifier;
        this.updateTime = LocalDateTime.now();
        this.content = version.getContent();
    }
}
