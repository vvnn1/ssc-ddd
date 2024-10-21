package indi.melon.ssc.draft.domain.configuration;

import indi.melon.ssc.draft.domain.draft.DraftID;
import indi.melon.ssc.domain.common.cqrs.AbstractAggregateRoot;
import indi.melon.ssc.draft.domain.configuration.event.AttachmentAllocated;
import indi.melon.ssc.draft.domain.configuration.event.AttachmentDeallocated;
import indi.melon.ssc.draft.domain.configuration.event.EngineAllocated;
import indi.melon.ssc.draft.domain.configuration.event.EngineDeallocated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.*;

/**
 * @author vvnn1
 * @since 2024/10/1 23:00
 */
@Getter
@Setter(AccessLevel.PACKAGE)
@ToString
public class Configuration extends AbstractAggregateRoot {
    private ConfigurationID id;
    private EngineID engineID;

    private Set<AttachmentID> attachmentIDCollection;


    private DraftID draftID;

    public Configuration() {
    }

    public Configuration(ConfigurationID id, DraftID draftID) {
        this.id = id;
        this.draftID = draftID;
    }
    public void assignAttachments(Collection<AttachmentID> assignedAttachmentIDCollection) {
        if (attachmentIDCollection == null && assignedAttachmentIDCollection == null){
            return;
        }

        if (assignedAttachmentIDCollection == null){
            attachmentIDCollection.stream()
                    .map(this::buildAttachmentDeallocated)
                    .forEach(this::addEvent);
            attachmentIDCollection = null;
            return;
        }

        if (attachmentIDCollection == null){
            assignedAttachmentIDCollection.stream()
                    .map(this::buildAttachmentAllocated)
                    .forEach(this::addEvent);
            attachmentIDCollection = new HashSet<>(assignedAttachmentIDCollection);
            return;
        }

        attachmentIDCollection.stream()
                .filter(attachmentID -> !assignedAttachmentIDCollection.contains(attachmentID))
                .map(this::buildAttachmentDeallocated)
                .forEach(this::addEvent);

        assignedAttachmentIDCollection.stream()
                .filter(attachmentID -> !attachmentIDCollection.contains(attachmentID))
                .map(this::buildAttachmentAllocated)
                .forEach(this::addEvent);
        attachmentIDCollection = new HashSet<>(assignedAttachmentIDCollection);
    }

    public void assignEngine(EngineID assignedEngineID){
        if (engineID == null && assignedEngineID == null){
            return;
        }

        if (assignedEngineID == null){
            addEvent(new EngineDeallocated(id.value, engineID.value));
            engineID = null;
            return;
        }

        if (engineID == null){
            addEvent(new EngineAllocated(id.value, assignedEngineID.value));
            engineID = assignedEngineID;
            return;
        }

        if (!Objects.equals(engineID, assignedEngineID)) {
            addEvent(new EngineAllocated(id.value, assignedEngineID.value));
            addEvent(new EngineDeallocated(id.value, engineID.value));
        }

        engineID = assignedEngineID;
    }

    private AttachmentDeallocated buildAttachmentDeallocated(AttachmentID attachmentID){
        return new AttachmentDeallocated(id.value, attachmentID.value);
    }

    private AttachmentAllocated buildAttachmentAllocated(AttachmentID attachmentID){
        return new AttachmentAllocated(id.value, attachmentID.value);
    }

    void setAttachmentIDCollection(Collection<AttachmentID> attachmentIDCollection) {
        if (attachmentIDCollection == null){
            this.attachmentIDCollection = null;
            return;
        }
        this.attachmentIDCollection = new HashSet<>(attachmentIDCollection);
    }

    public Collection<AttachmentID> getAttachmentIDCollection() {
        return Collections.unmodifiableCollection(attachmentIDCollection);
    }

}
