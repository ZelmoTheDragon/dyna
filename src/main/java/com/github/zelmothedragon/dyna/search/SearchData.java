package com.github.zelmothedragon.dyna.search;

import com.github.zelmothedragon.dyna.customer.Customer;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public enum SearchData {

    CUSTOMER("custormer", Customer.class);

    private final String typeName;

    private final Class<?> dataClass;

    private SearchData(final String typeName, final Class<?> dataClass) {
        this.typeName = typeName;
        this.dataClass = dataClass;
    }

    public String getTypeName() {
        return typeName;
    }

    public Class<?> getDataClass() {
        return dataClass;
    }

    public static Optional<SearchData> fromTypeName(final String typeName) {
        return Stream
                .of(values())
                .filter(e -> Objects.equals(e.typeName, typeName))
                .findFirst();
    }

}
