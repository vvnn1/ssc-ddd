package indi.melon.ssc.draft.domain.draft;

import indi.melon.ssc.domain.common.cqrs.AbstractAggregateRoot;
import indi.melon.ssc.draft.domain.configuration.event.AttachmentAllocated;
import indi.melon.ssc.draft.domain.configuration.event.AttachmentDeallocated;
import indi.melon.ssc.draft.domain.configuration.event.EngineAllocated;
import indi.melon.ssc.draft.domain.configuration.event.EngineDeallocated;
import indi.melon.ssc.draft.domain.exception.NotMatchException;
import indi.melon.ssc.draft.domain.template.Template;
import indi.melon.ssc.draft.domain.version.Version;
import jakarta.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author vvnn1
 * @since 2024/10/1 23:00
 */
@Getter
@Setter(AccessLevel.PACKAGE)
@ToString
public class Draft extends AbstractAggregateRoot {
    private DraftID id;
    private String name;
    private String content;
    private DraftCatalog catalog;
    private DraftType type;
    private String creator;
    private String modifier;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Configuration configuration;

    public Draft() {
    }

    Draft(DraftID id, String name, String content, DraftCatalog catalog, DraftType type, String creator) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.catalog = catalog;
        this.type = type;
        this.creator = creator;
        this.modifier = creator;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.configuration = new Configuration();
    }

    public Draft(DraftID id, String name, Template template, String creator) {
        this(
                id,
                name,
                template.getContent(),
                template.getCatalog().draftCatalog(),
                template.getType().draftType(),
                creator
        );
    }

    public Draft(DraftID id, String name, Draft draft, String creator) {
        this(
                id,
                name,
                draft.content,
                draft.catalog,
                draft.type,
                creator
        );
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
        Draft draft = new Draft(
                id,
                name,
                content,
                catalog,
                type,
                modifier
        );
        draft.assignEngine(configuration.engineId);
        draft.assignAttachments(Collections.emptyList());
        return draft;
    }

    public void assignEngine(EngineID engineId) {
        EngineID preEngineId = configuration.engineId;
        configuration = configuration.assignEngine(engineId);
        engineId = configuration.engineId;

        if (preEngineId == engineId) {
            return;
        }

        if (preEngineId != null) {
            addEvent(
                    new EngineDeallocated(
                            id.value,
                            preEngineId.value
                    )
            );
        }

        if (engineId != null) {
            addEvent(
                    new EngineAllocated(
                            id.value,
                            engineId.value
                    )
            );
        }
    }

    public void assignConfiguration(Configuration configuration) {
        if (configuration.engineId != null) {
            assignEngine(configuration.engineId);
        }

        if (configuration.attachmentIdCollection != null && !configuration.attachmentIdCollection.isEmpty()) {
            assignAttachments(configuration.attachmentIdCollection);
        }
    }

    public void assignAttachments(@Nonnull Collection<AttachmentID> attachmentIds) {
        Collection<AttachmentID> preAttachmentIds = configuration.getAttachmentIdCollection();
        configuration = configuration.assignAttachments(attachmentIds);
        attachmentIds = configuration.getAttachmentIdCollection();

        Set<AttachmentID> removedIds = new HashSet<>(preAttachmentIds);
        Set<AttachmentID> assignedIds = new HashSet<>(attachmentIds);

        removedIds.removeAll(assignedIds);
        assignedIds.removeAll(removedIds);

        for (AttachmentID removedId : removedIds) {
            addEvent(
                    new AttachmentDeallocated(
                            id.value,
                            removedId.value
                    )
            );
        }

        for (AttachmentID assignedId : assignedIds) {
            addEvent(
                    new AttachmentAllocated(
                            id.value,
                            assignedId.value
                    )
            );
        }
    }

    public void formatContent() {
        // TODO  待实现
    }
}
