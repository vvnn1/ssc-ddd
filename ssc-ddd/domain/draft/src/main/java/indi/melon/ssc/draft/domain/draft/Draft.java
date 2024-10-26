package indi.melon.ssc.draft.domain.draft;

import indi.melon.ssc.draft.domain.draft.exception.RollbackVersionNotMatchException;
import indi.melon.ssc.draft.domain.version.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author vvnn1
 * @since 2024/10/1 23:00
 */
@Getter
@Setter(AccessLevel.PACKAGE)
@ToString
public class Draft {
    private DraftID id;
    private String name;
    private String content;
    private DraftType type;
    private String creator;
    private String modifier;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer version;

    public Draft() {
    }

    public Draft(DraftID id, String name, DraftType type, String creator) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.creator = creator;
        this.modifier = creator;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public void rollback(Version version, String modifier) {
        if (!Objects.equals(id, version.getDraftID())){
            throw new RollbackVersionNotMatchException("not a match version: "+ version.getId() + " for draft: " + id);
        }
        this.content = version.getContent();
        this.modifier = modifier;
        this.updateTime = LocalDateTime.now();
    }

    public void rename(String newName, String modifier) {
        this.name = newName;
        this.modifier = modifier;
        this.updateTime = LocalDateTime.now();
    }

    public void editContent(String content, String modifier) {
        this.content = content;
        this.modifier = modifier;
        this.updateTime = LocalDateTime.now();
    }

    public Draft copy(DraftID id, String name, String modifier) {
        Draft copyDraft = new Draft(
                id,
                name,
                type,
                modifier
        );
        copyDraft.content = content;
        return copyDraft;
    }

    public void formatContent() {

    }
}
