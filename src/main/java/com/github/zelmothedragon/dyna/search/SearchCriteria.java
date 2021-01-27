package com.github.zelmothedragon.dyna.search;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SearchCriteria {

    private final List<SearchQuery> queries;

    public SearchCriteria() {
        this.queries = new ArrayList<>();
    }

    public void add(final SearchQuery query) {
        this.queries.add(query);
    }

    public Predicate toPredicate(
            final Root<?> root,
            final CriteriaBuilder cb) {

        var restrictions = new ArrayList<>(this.queries.size());
        for (var query : this.queries) {
            var path = root.get(query.getField());
            DynamicPredicate
                    .of(query.getOperator())
                    .map(e -> e.toPredicate(path, cb, query.getValues()))
                    .ifPresent(restrictions::add);
        }

        return cb.and(restrictions.toArray(new Predicate[0]));
    }

}
