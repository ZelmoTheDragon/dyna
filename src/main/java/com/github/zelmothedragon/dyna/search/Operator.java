package com.github.zelmothedragon.dyna.search;

import java.util.Objects;
import java.util.stream.Stream;

public enum Operator {

    EQUAL("eq"),
    NOT_EQUAL("neq"),
    LIKE("li"),
    NOT_LIKE("nli"),
    MATCH("ma"),
    NOT_MATCH("nma"),
    GREATER_THAN("gt"),
    GREATER_THAN_OR_EQUAL("ge"),
    LESS_THAN("lt"),
    LESS_THAN_OR_EQUAL("le"),
    EMPTY("");

    private final String code;

    private Operator(final String code) {
        this.code = code;
    }

    public static Operator of(final String code) {
        return Stream
                .of(values())
                .filter(e -> Objects.equals(e.code, code))
                .findFirst()
                .orElse(EMPTY);
    }

    public String getCode() {
        return code;
    }

}
