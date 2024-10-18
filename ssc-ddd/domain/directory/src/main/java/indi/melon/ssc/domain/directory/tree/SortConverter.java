package indi.melon.ssc.domain.directory.tree;

import jakarta.persistence.AttributeConverter;

/**
 * @author wangmenglong
 * @since 2024/10/16 10:38
 */
public class SortConverter implements AttributeConverter<Sort, String> {
    @Override
    public String convertToDatabaseColumn(Sort sort) {
        if (sort == null){
            return null;
        }
        return sort.toString();
    }

    /**
     *
     * @param order  the data from the database column to be
     *                converted
     *                ex: nameAsc.createTimeDesc
     * @return
     */
    @Override
    public Sort convertToEntityAttribute(String order) {
        if (order == null) {
            return null;
        }
        return Sort.deserialize(order);
    }
}
