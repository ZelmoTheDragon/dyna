package com.github.zelmothedragon.dyna.common.persistence;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class UUIDConverter implements AttributeConverter<UUID, String> {

    public UUIDConverter() {
    }

    @Override
    public String convertToDatabaseColumn(final UUID attribute) {
        String dbData;
        if (Objects.nonNull(attribute)) {
            dbData = attribute.toString();
        } else {
            dbData = null;
        }
        return dbData;
    }

    @Override
    public UUID convertToEntityAttribute(final String dbData) {
        UUID attribute;
        if (Objects.nonNull(dbData)) {
            attribute = UUID.fromString(dbData);
        } else {
            attribute = null;
        }
        return attribute;
    }

}
