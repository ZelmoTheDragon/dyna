package com.github.zelmothedragon.dyna.search;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

@FunctionalInterface
public interface DynamicPredicate<Y extends Comparable<? super Y>> {

    Predicate toPredicate(Path<Y> path, CriteriaBuilder cb, List<Y> data);

    static Optional<DynamicPredicate> of(final Operator operator) {
        DynamicPredicate predicate;
        switch (operator) {

            case EQUAL:
                predicate = DynamicPredicate::equal;
                break;

            case NOT_EQUAL:
                predicate = DynamicPredicate::notEqual;
                break;

            case LIKE:
                predicate = DynamicPredicate::like;
                break;

            case NOT_LIKE:
                predicate = DynamicPredicate::notLike;
                break;

            case MATCH:
                predicate = DynamicPredicate::match;
                break;

            case NOT_MATCH:
                predicate = DynamicPredicate::notMatch;
                break;

            case GREATER_THAN:
                predicate = DynamicPredicate::greaterThan;
                break;

            case GREATER_THAN_OR_EQUAL:
                predicate = DynamicPredicate::greaterThanOrEqualTo;
                break;

            case LESS_THAN:
                predicate = DynamicPredicate::lessThan;
                break;

            case LESS_THAN_OR_EQUAL:
                predicate = DynamicPredicate::lessThanOrEqualTo;
                break;

            default:
                predicate = null;
                break;
        }
        return Optional.ofNullable(predicate);
    }

    static <Y extends Comparable<? super Y>> Predicate equal(
            final Path<Y> path,
            final CriteriaBuilder cb,
            final List<Y> data) {

        var predicates = new ArrayList<Predicate>(data.size());
        for (var d : data) {
            var predicate = cb.equal(path, d);
            predicates.add(predicate);
        }

        return cb.or(predicates.toArray(new Predicate[0]));
    }

    static <Y extends Comparable<? super Y>> Predicate notEqual(
            final Path<Y> path,
            final CriteriaBuilder cb,
            final List<Y> data) {

        var predicates = new ArrayList<Predicate>(data.size());
        for (var d : data) {
            var predicate = cb.notEqual(path, d);
            predicates.add(predicate);
        }
        return cb.or(predicates.toArray(new Predicate[0]));
    }

    static <Y extends Comparable<? super Y>> Predicate like(
            final Path<Y> path,
            final CriteriaBuilder cb,
            final List<Y> data) {

        var expression = cb.lower(path.as(String.class));
        var predicates = new ArrayList<Predicate>(data.size());
        for (var d : data) {
            var value = "%" + unaccent(d.toString()) + "%";
            var predicate = cb.like(expression, value);
            predicates.add(predicate);
        }

        return cb.or(predicates.toArray(new Predicate[0]));
    }

    static <Y extends Comparable<? super Y>> Predicate notLike(
            final Path<Y> path,
            final CriteriaBuilder cb,
            final List<Y> data) {

        var expression = cb.lower(path.as(String.class));
        var predicates = new ArrayList<Predicate>(data.size());
        for (var d : data) {
            var value = "%" + unaccent(d.toString()) + "%";
            var predicate = cb.not(cb.like(expression, value));
            predicates.add(predicate);
        }
        return cb.or(predicates.toArray(new Predicate[0]));
    }

    static <Y extends Comparable<? super Y>> Predicate match(
            final Path<Y> path,
            final CriteriaBuilder cb,
            final List<Y> data) {

        var expression = cb.lower(path.as(String.class));
        var predicates = new ArrayList<Predicate>(data.size());
        for (var d : data) {
            var value = unaccent(d.toString());
            var predicate = cb.like(expression, value);
            predicates.add(predicate);
        }
        return cb.or(predicates.toArray(new Predicate[0]));
    }

    static <Y extends Comparable<Y>> Predicate notMatch(
            final Path<? extends Y> path,
            final CriteriaBuilder cb,
            final List<Y> data) {

        var expression = cb.lower(path.as(String.class));
        var predicates = new ArrayList<Predicate>(data.size());
        for (var d : data) {
            var value = unaccent(d.toString());
            var predicate = cb.not(cb.like(expression, value));
            predicates.add(predicate);
        }
        return cb.or(predicates.toArray(new Predicate[0]));
    }

    static <Y extends Comparable<? super Y>> Predicate greaterThan(
            final Path<Y> path,
            final CriteriaBuilder cb,
            final List<Y> data) {

        var predicates = new ArrayList<Predicate>(data.size());
        for (var d : data) {
            var predicate = cb.greaterThan(path, d);
            predicates.add(predicate);
        }
        return cb.or(predicates.toArray(new Predicate[0]));
    }

    static <Y extends Comparable<? super Y>> Predicate greaterThanOrEqualTo(
            final Path<Y> path,
            final CriteriaBuilder cb,
            final List<Y> data) {

        var predicates = new ArrayList<Predicate>(data.size());
        for (var d : data) {
            var predicate = cb.greaterThanOrEqualTo(path, d);
            predicates.add(predicate);
        }
        return cb.or(predicates.toArray(new Predicate[0]));
    }

    static <Y extends Comparable<? super Y>> Predicate lessThan(
            final Path<Y> path,
            final CriteriaBuilder cb,
            final List<Y> data) {

        var predicates = new ArrayList<Predicate>(data.size());
        for (var d : data) {
            var predicate = cb.lessThan(path, d);
            predicates.add(predicate);
        }
        return cb.or(predicates.toArray(new Predicate[0]));
    }

    static <Y extends Comparable<? super Y>> Predicate lessThanOrEqualTo(
            final Path<Y> path,
            final CriteriaBuilder cb,
            final List<Y> data) {

        var predicates = new ArrayList<Predicate>(data.size());
        for (var d : data) {
            var predicate = cb.lessThanOrEqualTo(path, d);
            predicates.add(predicate);
        }
        return cb.or(predicates.toArray(new Predicate[0]));
    }

    private static String unaccent(final String text) {
        var normalizedText = Normalizer.normalize(text.toLowerCase(), Normalizer.Form.NFD);
        var decomposed = new StringBuilder(normalizedText);
        for (var i = 0; i < decomposed.length(); i++) {
            if (decomposed.charAt(i) == '\u0141') {
                decomposed.deleteCharAt(i);
                decomposed.insert(i, '_');
            } else if (decomposed.charAt(i) == '\u0142') {
                decomposed.deleteCharAt(i);
                decomposed.insert(i, '_');
            }
        }
        var stripAccentsPattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return stripAccentsPattern.matcher(decomposed).replaceAll("_");
    }

}
