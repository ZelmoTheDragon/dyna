package com.github.zelmothedragon.dyna.search;

import java.util.List;
import java.util.Objects;

public final class SearchQuery {

    public static final String PARAM_PAGE_SIZE = "pageSize";

    public static final String PARAM_PAGE_NUMBER = "pageNumber";

    public static final String PARAM_SORT = "sort";

    private final String field;

    private final List<String> values;

    private final Operator operator;

    public SearchQuery(final String field, final List<String> values, final String operator) {
        this.field = field;
        this.values = values;
        this.operator = Operator.of(operator);
    }

    @Override
    public boolean equals(Object obj) {
        final boolean eq;
        if (this == obj) {
            eq = true;
        } else if (!(obj instanceof SearchQuery)) {
            eq = false;
        } else {
            var other = (SearchQuery) obj;
            eq = Objects.equals(this.field, other.field)
                    && Objects.equals(this.values, other.values)
                    && Objects.equals(this.operator, other.operator);
        }
        return eq;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.field, this.values, this.operator);
    }

    @Override
    public String toString() {
        return String.format(
                "%s{field='%s', values='%s', operator='%s'}",
                getClass().getName(),
                this.field,
                this.values,
                this.operator
        );
    }

    public int getNumericValue() {
        int number;
        if (this.values.isEmpty()) {
            number = -1;
        } else {
            var text = this.values.get(0);
            if (text.chars().allMatch(Character::isDigit)) {
                number = Integer.valueOf(text);
            } else {
                number = -1;
            }
        }
        return number;
    }

    public boolean isEmpty() {
        return Objects.equals(this.operator, Operator.EMPTY)
                || this.values.isEmpty();
    }

    public boolean isPageSize() {
        return Objects.equals(this.field, PARAM_PAGE_SIZE);
    }

    public boolean isPageNumber() {
        return Objects.equals(this.field, PARAM_PAGE_NUMBER);
    }

    public boolean isSort() {
        return Objects.equals(this.field, PARAM_SORT);
    }

    public String getField() {
        return field;
    }

    public List<String> getValues() {
        return values;
    }

    public Operator getOperator() {
        return operator;
    }

}
