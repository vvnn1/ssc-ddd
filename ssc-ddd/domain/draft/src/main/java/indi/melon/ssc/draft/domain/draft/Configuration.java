package indi.melon.ssc.draft.domain.draft;

import indi.melon.ssc.domain.common.cqrs.AbstractAggregateRoot;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author vvnn1
 * @since 2024/10/1 23:00
 */
@Getter
@Setter(AccessLevel.PACKAGE)
@ToString
public final class Configuration extends AbstractAggregateRoot {
    final EngineID engineId;
    final Set<AttachmentID> attachmentIdCollection;
    public Configuration() {
        this.engineId = null;
        this.attachmentIdCollection = Collections.emptySet();
    }

    private Configuration(EngineID engineId, Set<AttachmentID> attachmentIdCollection) {
        this.engineId = engineId;
        this.attachmentIdCollection = attachmentIdCollection;
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
                new HashSet<>(attachmentIdCollection)
        );
    }

    public Collection<AttachmentID> getAttachmentIdCollection() {
        return Collections.unmodifiableCollection(attachmentIdCollection);
    }

    public Collection<AttachmentID> attachmentsNotIn(Configuration configuration){
        return attachmentIdCollection.stream()
                .filter(id -> !configuration.attachmentIdCollection.contains(id))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return Objects.equals(engineId, that.engineId) && Objects.equals(attachmentIdCollection, that.attachmentIdCollection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(engineId, attachmentIdCollection);
    }
}
