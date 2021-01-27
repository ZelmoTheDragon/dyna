package com.github.zelmothedragon.dyna.common.persistence.entity;

public interface Identifiable<K> {

    K getId();

    void setId(K id);
}
