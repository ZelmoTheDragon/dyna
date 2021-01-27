package com.github.zelmothedragon.dyna.search;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public enum DynamicType {

    BOOLEAN(Boolean.class, Boolean::valueOf),
    BYTE(Byte.class, Byte::valueOf),
    SHORT(Short.class, Short::valueOf),
    INTEGER(Integer.class, Integer::valueOf),
    LONG(Long.class, Long::valueOf),
    FLOAT(Float.class, Float::valueOf),
    DOUBLE(Double.class, Double::valueOf),
    STRING(String.class, String::valueOf),
    LOCAL_DATE(LocalDate.class, DynamicType::parseLocalDate),
    LOCAL_TIME(LocalTime.class, DynamicType::parseLocalTime),
    LOCAL_DATE_TIME(LocalDateTime.class, DynamicType::parseLocalDateTime),
    ENUM(Enum.class, DynamicType::parseEnum);

    private final Class<?> type;

    private final Function<String, Object> parser;

    DynamicType(
            final Class<?> type,
            final Function<String, Object> parser) {

        this.type = type;
        this.parser = parser;
    }

    public <T> T apply(final String value) {
        return (T) this.parser.apply(value);
    }

    public static DynamicType of(final Class<?> type) {
        return Stream.of(values())
                .filter(e -> Objects.equals(e.type, type))
                .findFirst()
                .orElse(BOOLEAN);
    }

    private static LocalDate parseLocalDate(final String text) {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(text, formatter);
    }

    private static LocalTime parseLocalTime(final String text) {
        var formatter = DateTimeFormatter.ofPattern("HH-mm-ss");
        return LocalTime.parse(text, formatter);
    }

    private static LocalDateTime parseLocalDateTime(final String text) {
        var formatter = DateTimeFormatter.ofPattern("HH-mm-ss");
        return LocalDateTime.parse(text, formatter);
    }

    private static <T> T parseEnum(final String text) {
        // TODO
        return null;
    }

}
