package com.github.zelmothedragon.dyna.common.persistence.repository;

import com.github.zelmothedragon.dyna.common.persistence.entity.Identifiable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface Repository<E extends Identifiable<K>, K> {

    boolean contains(E entity);

    boolean containsAll(Collection<E> entities);

    boolean isEmpty();

    long size();

    E add(E entity);

    List<E> addAll(Collection<E> entities);

    boolean remove(E entity);

    boolean removeAll(Collection<E> entities);

    Optional<E> get(K id);

    List<E> getAll();

}
