package indi.melon.ssc.draft.domain.draft;

import indi.melon.ssc.domain.common.cqrs.AbstractAggregateRoot;
import indi.melon.ssc.draft.domain.draft.event.AttachmentAllocated;
import indi.melon.ssc.draft.domain.draft.event.AttachmentDeallocated;
import indi.melon.ssc.draft.domain.draft.event.EngineAllocated;
import indi.melon.ssc.draft.domain.draft.event.EngineDeallocated;
import indi.melon.ssc.draft.domain.exception.NotMatchException;
import indi.melon.ssc.draft.domain.template.Template;
import indi.melon.ssc.draft.domain.version.Version;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author vvnn1
 * @since 2024/10/1 23:00
 */
@Getter
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
        draft.assignEngine(configuration.currentEngineId());
        draft.assignAttachments(Collections.emptyList());
        return draft;
    }

    public void assignEngine(EngineID engineId) {
        if (Objects.equals(configuration.currentEngineId(), engineId)) {
            return;
        }

        EngineID preEngineId = configuration.currentEngineId();
        configuration = configuration.assignEngine(engineId);
        engineId = configuration.currentEngineId();

        if (preEngineId != null) {
            addEvent(
                    new EngineDeallocated(
                            id,
                            preEngineId
                    )
            );
        }

        if (engineId != null) {
            addEvent(
                    new EngineAllocated(
                            id,
                            engineId
                    )
            );
        }
    }

    public void assignConfiguration(Draft fromDraft) {
        assignConfiguration(fromDraft.configuration);
    }

    public void assignConfiguration(Configuration configuration) {
        assignEngine(configuration.currentEngineId());
        assignAttachments(configuration.currentAttachmentIds());
    }

    public void assignAttachments(@Nonnull Collection<AttachmentID> attachmentIds) {
        Collection<AttachmentID> preAttachmentIds = configuration.currentAttachmentIds();
        configuration = configuration.assignAttachments(attachmentIds);
        attachmentIds = configuration.currentAttachmentIds();

        Set<AttachmentID> removedIds = new HashSet<>(preAttachmentIds);
        Set<AttachmentID> assignedIds = new HashSet<>(attachmentIds);

        removedIds.removeAll(assignedIds);
        assignedIds.removeAll(preAttachmentIds);

        for (AttachmentID removedId : removedIds) {
            addEvent(
                    new AttachmentDeallocated(
                            id,
                            removedId
                    )
            );
        }

        for (AttachmentID assignedId : assignedIds) {
            addEvent(
                    new AttachmentAllocated(
                            id,
                            assignedId
                    )
            );
        }
    }

    public void formatContent() {
        // TODO  待实现
    }
}
