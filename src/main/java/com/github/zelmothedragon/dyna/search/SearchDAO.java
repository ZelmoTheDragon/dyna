package com.github.zelmothedragon.dyna.search;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;

@Dependent
public class SearchDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager em;

    public SearchDAO() {
    }

    public List<?> filter(final Class<?> entityClass, final Set<SearchQuery> queries) {

        var cb = em.getCriteriaBuilder();
        var cq = cb.createQuery(entityClass);
        var root = cq.from(entityClass);
        var combinationOfPredicate = createPredicate(root, cb, queries);

        cq.where(combinationOfPredicate);

        var pageSize = queries
                .stream()
                .filter(SearchQuery::isPageSize)
                .mapToInt(SearchQuery::getNumericValue)
                .map(e -> Math.max(e, Pagination.DEFAULT_PAGE_SIZE))
                .findFirst()
                .orElse(Pagination.DEFAULT_PAGE_SIZE);

        var pageNumber = queries
                .stream()
                .filter(SearchQuery::isPageNumber)
                .mapToInt(SearchQuery::getNumericValue)
                .map(e -> Math.min(e, Pagination.DEFAULT_PAGE_NUMBER))
                .findFirst()
                .orElse(Pagination.DEFAULT_PAGE_NUMBER);

        return em
                .createQuery(cq)
                .setMaxResults(pageSize)
                .setFirstResult(pageNumber * pageSize)
                .getResultList();
    }

    public int count(final Class<?> entityClass, final Set<SearchQuery> queries) {

        var cb = em.getCriteriaBuilder();
        var cq = cb.createQuery(Long.class);
        var root = cq.from(entityClass);
        var combinationOfPredicate = createPredicate(root, cb, queries);

        cq.select(cb.count(root));
        cq.where(combinationOfPredicate);

        return em
                .createQuery(cq)
                .getSingleResult()
                .intValue();
    }

    private static boolean attributeExist(
            final Set<Attribute<?, ?>> attributes,
            final String fieldName) {

        return attributes
                .stream()
                .filter(e -> Objects.equals(e.getName(), fieldName))
                .filter(e -> e.getPersistentAttributeType() == Attribute.PersistentAttributeType.BASIC)
                .count() >= 1;
    }

    private static Predicate createPredicate(
            final Root<?> root,
            final CriteriaBuilder cb,
            final Set<SearchQuery> queries) {

        var attributes = (Set<Attribute<?, ?>>) root.getModel().getAttributes();
        var criteria = new SearchCriteria();
        queries
                .stream()
                .filter(e -> !e.isEmpty())
                .filter(e -> attributeExist(attributes, e.getField()))
                .forEach(criteria::add);

        return criteria.toPredicate(root, cb);
    }

}
