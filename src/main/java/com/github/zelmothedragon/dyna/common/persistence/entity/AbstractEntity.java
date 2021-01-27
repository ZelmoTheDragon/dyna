package com.github.zelmothedragon.dyna.common.persistence.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbPropertyOrder;
import javax.json.bind.config.PropertyOrderStrategy;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@JsonbPropertyOrder(PropertyOrderStrategy.LEXICOGRAPHICAL)
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractEntity implements Identifiable<UUID>, Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @JsonbProperty(value = "id", nillable = false)
    @Id
    @Column(name = "id", nullable = false, unique = true, columnDefinition = "CHAR(36)")
    protected UUID id;

    @NotNull
    @JsonbProperty(value = "version", nillable = false)
    @Version
    @Column(name = "version", nullable = false)
    protected Long version;

    protected AbstractEntity() {
        this.id = UUID.randomUUID();
        this.version = 0L;
    }

    @Override
    public boolean equals(Object obj) {
        final boolean eq;
        if (this == obj) {
            eq = true;
        } else if (!(obj instanceof AbstractEntity)) {
            eq = false;
        } else {
            AbstractEntity other = (AbstractEntity) obj;
            eq = Objects.equals(this.id, other.id)
                    && Objects.equals(this.version, other.version);
        }
        return eq;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version);
    }

    @Override
    public String toString() {
        return String.format(
                "%s{id=%s, version=%s}",
                getClass().getName(),
                id,
                version
        );
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
