package indi.melon.ssc.draft.domain.draft;

import indi.melon.ssc.domain.common.cqrs.AbstractAggregateRoot;
import jakarta.annotation.Nonnull;
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
public final class Configuration extends AbstractAggregateRoot {
    final EngineID engineId;
    final Set<AttachmentID> attachmentIdSet;
    public Configuration() {
        this.engineId = null;
        this.attachmentIdSet = Collections.emptySet();
    }

    public Configuration(EngineID engineId, @Nonnull Collection<AttachmentID> attachmentIdCollection) {
        this.engineId = engineId;
        this.attachmentIdSet = new HashSet<>(attachmentIdCollection);
    }

    public Configuration assignAttachments(@Nonnull Collection<AttachmentID> assignedAttachmentIDCollection) {
        return new Configuration(
                engineId,
                new HashSet<>(assignedAttachmentIDCollection)
        );
    }

    public Configuration assignEngine(EngineID assignedEngineID){
        return new Configuration(
                assignedEngineID,
                new HashSet<>(attachmentIdSet)
        );
    }

    public EngineID currentEngineId() {
        return engineId;
    }

    public Collection<AttachmentID> currentAttachmentIds() {
        return Collections.unmodifiableCollection(attachmentIdSet);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return Objects.equals(engineId, that.engineId) && Objects.equals(attachmentIdSet, that.attachmentIdSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(engineId, attachmentIdSet);
    }
}
