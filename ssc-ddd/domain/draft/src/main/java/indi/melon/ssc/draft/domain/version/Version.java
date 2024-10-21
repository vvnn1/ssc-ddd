package indi.melon.ssc.draft.domain.version;

import indi.melon.ssc.draft.domain.draft.DraftID;
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
public class Version {
    private VersionID id;
    private String content;
    private LocalDateTime createTime;
    private String creator;
    private String remark;
    private Boolean locked;
    private DraftID draftID;

    public Version() {
    }

    public Version(VersionID id, DraftID draftID, String content, String remark, String creator) {
        this.id = id;
        this.draftID = draftID;
        this.content = content;
        this.remark = remark;
        this.creator = creator;
        this.createTime = LocalDateTime.now();
        this.locked = false;
    }

    public void lock(){
        this.locked = true;
    }

    public void unlock() {
        this.locked = false;
    }
}
