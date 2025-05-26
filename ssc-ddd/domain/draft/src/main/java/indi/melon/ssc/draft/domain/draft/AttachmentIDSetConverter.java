package indi.melon.ssc.draft.domain.draft;

import jakarta.persistence.AttributeConverter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangmenglong
 * @since 2024/10/22 10:24
 */
public class AttachmentIDSetConverter implements AttributeConverter<Set<AttachmentID>, String> {

    @Override
    public String convertToDatabaseColumn(Set<AttachmentID> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }

        return attribute.stream()
                .map(AttachmentID::getValue)
                .collect(Collectors.joining(","));
    }

    @Override
    public Set<AttachmentID> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()){
            return new HashSet<>();
        }

        return Arrays.stream(dbData.split(","))
                .map(AttachmentID::new)
                .collect(Collectors.toSet());
    }
}
