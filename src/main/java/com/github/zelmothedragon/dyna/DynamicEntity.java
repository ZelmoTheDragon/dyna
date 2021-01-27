package com.github.zelmothedragon.dyna;

import com.github.zelmothedragon.dyna.common.persistence.entity.Identifiable;
import com.github.zelmothedragon.dyna.customer.Customer;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public enum DynamicEntity {

    CUSTOMER("customer", Customer.class, UUID::fromString);

    private final String typeName;

    private final Class<? extends Identifiable<?>> entityClass;

    private final Function<String, Object> identifierConverter;

    private DynamicEntity(
            final String typeName,
            final Class<? extends Identifiable<?>> entityClass,
            final Function<String, Object> identifierConverter) {

        this.typeName = typeName;
        this.entityClass = entityClass;
        this.identifierConverter = identifierConverter;
    }

    public static Optional<DynamicEntity> fromTypeName(final String typeName) {
        return Stream
                .of(values())
                .filter(e -> Objects.equals(e.typeName, typeName))
                .findFirst();
    }

    public Object convertAsIdentifier(final String id) {
        return identifierConverter.apply(id);
    }

    public String getTypeName() {
        return typeName;
    }

    public Class<? extends Identifiable<?>> getEntityClass() {
        return entityClass;
    }

}
