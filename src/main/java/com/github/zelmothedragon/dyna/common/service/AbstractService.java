package com.github.zelmothedragon.dyna.common.service;

import com.github.zelmothedragon.dyna.common.persistence.entity.Identifiable;
import com.github.zelmothedragon.dyna.common.persistence.repository.Repository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class AbstractService<E extends Identifiable<K>, K, D extends Repository<E, K>>
        implements Service<E, K> {

    protected AbstractService() {
    }

    protected abstract D getRepository();

    @Override
    public E save(E entity) {
        return getRepository().add(entity);
    }

    @Override
    public void remove(E entity) {
        getRepository().remove(entity);
    }

    @Override
    public Optional<E> findById(K id) {
        return getRepository().get(id);
    }

    @Override
    public List<E> find() {
        return Collections.unmodifiableList(getRepository().getAll());
    }

}
