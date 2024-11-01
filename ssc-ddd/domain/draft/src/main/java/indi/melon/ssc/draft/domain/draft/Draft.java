package indi.melon.ssc.draft.domain.draft;

import indi.melon.ssc.draft.domain.draft.exception.NotMatchException;
import indi.melon.ssc.draft.domain.template.Template;
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
    private DraftCatalog catalog;
    private DraftType type;
    private String creator;
    private String modifier;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Draft() {
    }

    Draft(DraftID id, String name, DraftCatalog catalog, String creator) {
        this.id = id;
        this.name = name;
        this.catalog = catalog;
        this.creator = creator;
        this.modifier = creator;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public Draft(DraftID id, String name, Template template, String creator) {
        this.id = id;
        this.name = name;
        this.content = template.getContent();
        this.catalog = template.getCatalog().draftCatalog();
        this.type = template.getType().draftType();
        this.creator = creator;
        this.modifier = creator;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public Draft(DraftID id, String name, Draft draft, String creator) {
        this.id = id;
        this.name = name;
        this.content = draft.content;
        this.catalog = draft.catalog;
        this.type = draft.type;
        this.creator = creator;
        this.modifier = creator;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public void rollback(Version version, String modifier) {
        if (!Objects.equals(id, version.getDraftID())){
            throw new NotMatchException("not a match version: "+ version.getId() + " for draft: " + id);
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
                catalog,
                modifier
        );
        copyDraft.content = content;
        return copyDraft;
    }

    public void formatContent() {

    }
}
