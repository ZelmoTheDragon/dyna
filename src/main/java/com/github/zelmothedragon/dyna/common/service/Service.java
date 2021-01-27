package com.github.zelmothedragon.dyna.common.service;

import com.github.zelmothedragon.dyna.common.persistence.entity.Identifiable;
import java.util.List;
import java.util.Optional;

public interface Service<E extends Identifiable<K>, K> {

    E save(E entity);

    void remove(E entity);

    Optional<E> findById(K id);

    List<E> find();
}
