package indi.melon.ssc.draft.south.publisher.event;

import indi.melon.ssc.draft.domain.draft.event.AttachmentDeallocated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author wangmenglong
 * @since 2025/5/29 19:40
 */
@Mapper
public interface XAttachmentDeallocatedConverter extends XCommonConverter {
    XAttachmentDeallocatedConverter MAPPER = Mappers.getMapper(XAttachmentDeallocatedConverter.class);

    @Mapping(target = "draftId", source = "event.draftId", qualifiedByName = "toString")
    @Mapping(target = "attachmentId", source = "event.attachmentId", qualifiedByName = "toString")
    @Mapping(target = "topic", source = "topic")
    @Mapping(target = "tag", source = "tag")
    @Mapping(target = "key", source = "key")
    XAttachmentDeallocated from(AttachmentDeallocated event, String topic, String tag, String key);
}
