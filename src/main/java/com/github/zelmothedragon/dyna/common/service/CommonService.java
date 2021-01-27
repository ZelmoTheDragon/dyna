package com.github.zelmothedragon.dyna.common.service;

import com.github.zelmothedragon.dyna.common.persistence.entity.Identifiable;
import java.util.Optional;

import javax.inject.Inject;

import com.github.zelmothedragon.dyna.common.persistence.repository.GenericDAO;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class CommonService {

    @Inject
    private GenericDAO dao;

    public CommonService() {
    }

    public void save(final Identifiable<?> entity) {
        dao.add(entity);
    }

    public void remove(final Identifiable<?> entity) {
        dao.remove(entity);
    }

    public List<? extends Identifiable<?>> find(final Class<? extends Identifiable<?>> entityClass) {
        return dao.getAll(entityClass);
    }

    public Optional<? extends Identifiable<?>> findById(final Class<? extends Identifiable<?>> entityClass, final Object id) {
        return dao.get(entityClass, id);
    }
}
