package indi.melon.ssc.draft.south.publisher.event;

import indi.melon.ssc.draft.domain.draft.event.EngineAllocated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


/**
 * @author wangmenglong
 * @since 2025/5/29 14:21
 */
@Mapper
public interface XEngineAllocatedConverter extends XCommonConverter {
    XEngineAllocatedConverter MAPPER = Mappers.getMapper(XEngineAllocatedConverter.class);

    @Mapping(target = "draftId", source = "event.draftId", qualifiedByName = "toString")
    @Mapping(target = "engineId", source = "event.engineId", qualifiedByName = "toString")
    @Mapping(target = "topic", source = "topic")
    @Mapping(target = "tag", source = "tag")
    @Mapping(target = "key", source = "key")
    XEngineAllocated from(EngineAllocated event, String topic, String tag, String key);
}
