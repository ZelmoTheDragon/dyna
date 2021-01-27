package com.github.zelmothedragon.dyna.search;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public enum DynamicType {

    BOOLEAN(Boolean.class, Boolean::parseBoolean),
    BYTE(Byte.class, Byte::parseByte),
    SHORT(Short.class, Short::parseShort),
    
    ;

    private final Class<?> type;

    private final Function<String, Object> parser;

    DynamicType(
            final Class<?> type,
            final Function<String, Object> parser) {

        this.type = type;
        this.parser = parser;
    }

    public static DynamicType of(final Class<?> type) {
        return Stream.of(values())
                .filter(e -> Objects.equals(e.type, type))
                .findFirst()
                .orElse(BOOLEAN);
    }

}
