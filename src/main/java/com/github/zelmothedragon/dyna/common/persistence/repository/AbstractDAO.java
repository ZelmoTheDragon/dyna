package com.github.zelmothedragon.dyna.common.persistence.repository;

import com.github.zelmothedragon.dyna.common.persistence.entity.Identifiable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

public abstract class AbstractDAO<E extends Identifiable<K>, K> implements Repository<E, K> {

    @Inject
    protected transient EntityManager em;

    protected AbstractDAO() {
    }

    @Override
    public boolean contains(final E entity) {
        boolean exists;
        if (em.contains(entity)) {
            exists = true;
        } else {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<E> root = query.from(getEntityClass());
            query.select(cb.count(root));
            K pk = getIdentifier(entity);
            SingularAttribute<? super E, ?> pkType = getIdentifierType(entity);
            Predicate predicate = cb.equal(root.get(pkType), pk);
            query.where(predicate);
            Long result = em.createQuery(query).getSingleResult();
            exists = result == 1L;
        }
        return exists;
    }

    @Override
    public boolean containsAll(final Collection<E> entities) {
        return entities.stream().allMatch(this::contains);
    }

    @Override
    public boolean isEmpty() {
        return size() == 0L;
    }

    @Override
    public long size() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<E> root = q.from(getEntityClass());
        q.select(cb.count(root));
        return em.createQuery(q).getSingleResult();
    }

    @Override
    public E add(final E entity) {
        E attachedEntity;
        if (contains(entity)) {
            attachedEntity = em.merge(entity);
        } else {
            em.persist(entity);
            attachedEntity = entity;
        }
        return attachedEntity;
    }

    @Override
    public List<E> addAll(final Collection<E> entities) {
        return entities
                .stream()
                .map(this::add)
                .collect(Collectors.toList());
    }

    @Override
    public boolean remove(final E entity) {
        K id = getIdentifier(entity);
        E attachedEntity = em.getReference(getEntityClass(), id);
        em.remove(attachedEntity);
        CDI.current().destroy(attachedEntity);
        return true;
    }

    @Override
    public boolean removeAll(final Collection<E> entities) {
        entities.forEach(this::remove);
        return true;
    }

    @Override
    public Optional<E> get(final K id) {
        E entity = em.find(getEntityClass(), id);
        return Optional.ofNullable(entity);
    }

    @Override
    public List<E> getAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> q = cb.createQuery(getEntityClass());
        Root<E> root = q.from(getEntityClass());
        q.select(root);
        return em
                .createQuery(q)
                .getResultList();
    }

    protected K getIdentifier(final E entity) {
        return (K) em
                .getEntityManagerFactory()
                .getPersistenceUnitUtil()
                .getIdentifier(entity);
    }

    protected SingularAttribute<? super E, ?> getIdentifierType(final E entity) {
        K pk = getIdentifier(entity);
        Class<? extends Object> pkClass = pk.getClass();
        return em
                .getMetamodel()
                .entity(getEntityClass())
                .getId(pkClass);
    }

    protected Class<E> getEntityClass() {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<E> entityClass = (Class<E>) superClass.getActualTypeArguments()[0];
        return entityClass;
    }

}
