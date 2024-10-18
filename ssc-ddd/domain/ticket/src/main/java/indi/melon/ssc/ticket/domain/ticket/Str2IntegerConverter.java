package indi.melon.ssc.ticket.domain.ticket;

import jakarta.persistence.AttributeConverter;

/**
 * @author wangmenglong
 * @since 2024/10/17 17:19
 */
public class Str2IntegerConverter implements AttributeConverter<Long, String> {
    @Override
    public String convertToDatabaseColumn(Long attribute) {
        if (attribute == null) {
            return null;
        }
        return String.valueOf(attribute);
    }

    @Override
    public Long convertToEntityAttribute(String dbData) {
        if (dbData == null){
            return null;
        }
        return Long.parseLong(dbData);
    }
}
