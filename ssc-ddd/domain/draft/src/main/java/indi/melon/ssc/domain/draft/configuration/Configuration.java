package indi.melon.ssc.domain.draft.configuration;

import indi.melon.ssc.domain.draft.draft.DraftID;
import indi.melon.ssc.domain.common.cqrs.AbstractAggregateRoot;
import indi.melon.ssc.domain.draft.configuration.event.AttachmentAllocated;
import indi.melon.ssc.domain.draft.configuration.event.AttachmentDeallocated;
import indi.melon.ssc.domain.draft.configuration.event.EngineAllocated;
import indi.melon.ssc.domain.draft.configuration.event.EngineDeallocated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private List<AttachmentID> attachmentIDList;
    private DraftID draftID;

    public void assignAttachments(List<AttachmentID> assignedAttachmentIDList) {
        attachmentIDList.stream()
                .filter(attachmentID -> !assignedAttachmentIDList.contains(attachmentID))
                .map(attachmentID ->  new AttachmentDeallocated(id, attachmentID))
                .forEach(this::addEvent);


        assignedAttachmentIDList.stream()
                .filter(attachmentID -> !attachmentIDList.contains(attachmentID))
                .map(attachmentID -> new AttachmentAllocated(id, attachmentID))
                .forEach(this::addEvent);

        attachmentIDList = new ArrayList<>(assignedAttachmentIDList);
    }

    public void assignEngine(EngineID assignedEngineID){
        if (!Objects.equals(engineID, assignedEngineID)) {
            addEvent(new EngineAllocated(id, assignedEngineID));
            addEvent(new EngineDeallocated(id, engineID));
        }

        engineID = assignedEngineID;
    }
}
