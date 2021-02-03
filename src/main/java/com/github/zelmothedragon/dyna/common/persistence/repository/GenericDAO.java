package com.github.zelmothedragon.dyna.common.persistence.repository;

import com.github.zelmothedragon.dyna.common.persistence.entity.Identifiable;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Dependent
public class GenericDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    protected transient EntityManager em;

    public GenericDAO() {
    }

    public static GenericDAO of() {
        return CDI.current().select(GenericDAO.class).get();
    }

    public boolean contains(final Identifiable<?> entity) {
        boolean exists;
        Class<? extends Identifiable> entityClass = entity.getClass();
        if (Objects.isNull(entity.getId())) {
            exists = false;
        } else if (em.contains(entity)) {
            exists = true;
        } else {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<? extends Identifiable> root = query.from(entityClass);
            query.select(cb.count(root));
            Object id = getIdentifier(entity);
            String idName = getIdentifierName(entity);
            Predicate predicate = cb.equal(root.get(idName), id);
            query.where(predicate);
            Long result = em.createQuery(query).getSingleResult();
            exists = result == 1L;
        }
        return exists;
    }

    public boolean containsAll(final Collection<? extends Identifiable<?>> entities) {
        return entities.stream().allMatch(this::contains);
    }

    public boolean isEmpty(final Class<? extends Identifiable<?>> entityClass) {
        return size(entityClass) == 0L;
    }

    public long size(final Class<? extends Identifiable<?>> entityClass) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<? extends Identifiable<?>> root = q.from(entityClass);
        q.select(cb.count(root));
        return em.createQuery(q).getSingleResult();
    }

    public Identifiable<?> add(final Identifiable<?> entity) {
        Identifiable<?> managedEntity;
        if (contains(entity)) {
            managedEntity = em.merge(entity);
        } else {
            em.persist(entity);
            managedEntity = entity;
        }
        return managedEntity;
    }

    public List<? extends Identifiable<?>> addAll(final Collection<? extends Identifiable<?>> entities) {
        return entities.stream().map(this::add).collect(Collectors.toList());
    }

    public void remove(final Identifiable<?> entity) {
        Class<? extends Identifiable> entityClass = entity.getClass();
        Object id = getIdentifier(entity);
        Identifiable managedEntity = em.getReference(entityClass, id);
        em.remove(managedEntity);
        CDI.current().destroy(managedEntity);
    }

    public void removeAll(final Collection<? extends Identifiable<?>> entities) {
        entities.forEach(this::remove);
    }

    public Optional<? extends Identifiable<?>> get(final Class<? extends Identifiable<?>> entityClass, final Object id) {
        Optional<? extends Identifiable<?>> option;
        if (Objects.isNull(id)) {
            option = Optional.empty();
        } else {
            Identifiable<?> entity = em.find(entityClass, id);
            option = Optional.ofNullable(entity);
        }
        return option;
    }

    public List<? extends Identifiable<?>> getAll(final Class<? extends Identifiable<?>> entityClass) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<? extends Identifiable<?>> q = cb.createQuery(entityClass);
        q.from(entityClass);
        return em.createQuery(q).getResultList();
    }

    private Object getIdentifier(final Identifiable<?> entity) {
        return em
                .getEntityManagerFactory()
                .getPersistenceUnitUtil()
                .getIdentifier(entity);
    }

    private String getIdentifierName(final Identifiable<?> entity) {
        Class<? extends Identifiable> entityClass = entity.getClass();
        Object id = getIdentifier(entity);
        Class<? extends Object> idClass = id.getClass();
        return em
                .getMetamodel()
                .entity(entityClass)
                .getId(idClass)
                .getName();
    }

}
