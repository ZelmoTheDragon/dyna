package com.github.zelmothedragon.dyna.search;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SearchService {

    private static final String BEGIN_BRACKET_TOKEN = "[";

    private static final String END_BRACKET_TOKEN = "]";

    private static final String OR_TOKEN_REGEX = "\\|";

    @Inject
    private SearchDAO dao;

    public SearchService() {
    }

    public Pagination search(final String typeName, final Set<Map.Entry<String, List<String>>> parameters) {

        var queries = convertParameters(parameters);

        var entities = SearchData
                .fromTypeName(typeName)
                .map(SearchData::getDataClass)
                .map(e -> this.dao.filter(e, queries))
                .orElseGet(Collections::emptyList);

        var size = SearchData
                .fromTypeName(typeName)
                .map(SearchData::getDataClass)
                .map(e -> this.dao.count(e, queries))
                .orElse(0);

        var pageNumber = extractNumericField(
                parameters,
                SearchQuery.PARAM_PAGE_NUMBER,
                Pagination.DEFAULT_PAGE_NUMBER
        );

        var pageSize = extractNumericField(
                parameters,
                SearchQuery.PARAM_PAGE_SIZE,
                Pagination.DEFAULT_PAGE_SIZE
        );

        var pagination = new Pagination();
        pagination.setEntity(typeName);
        pagination.setPageNumber(pageNumber);
        pagination.setPageSize(pageSize);
        pagination.setCount(size);
        pagination.setData((List<Object>) entities);
        return pagination;
    }

    private static Set<SearchQuery> convertParameters(final Set<Map.Entry<String, List<String>>> parameters) {
        var queries = new LinkedHashSet<SearchQuery>(parameters.size());

        for (var entry : parameters) {
            final var rawKey = entry.getKey();
            for (String rawValue : entry.getValue()) {
                var tuple = extractFieldAndOperator(rawKey);
                var fiel = tuple.getKey();
                var operator = tuple.getValue();
                var values = rawValue.split(OR_TOKEN_REGEX);
                var query = new SearchQuery(fiel, List.of(values), operator);
                queries.add(query);
            }
        }
        return queries;
    }

    private static boolean hasOperator(final String text) {
        return text.contains(BEGIN_BRACKET_TOKEN)
                && text.contains(END_BRACKET_TOKEN);
    }

    private static Map.Entry<String, String> extractFieldAndOperator(final String text) {
        String field;
        String operator;
        if (hasOperator(text)) {
            var beginBracket = text.indexOf(BEGIN_BRACKET_TOKEN);
            var endBracket = text.indexOf(END_BRACKET_TOKEN);
            operator = text.substring(beginBracket + 1, endBracket);
            field = text.substring(0, beginBracket);
        } else {
            operator = "";
            field = text;
        }
        return Map.entry(field, operator);
    }

    private static int extractNumericField(
            final Set<Map.Entry<String, List<String>>> parameters,
            final String keyName,
            final int defaultValue) {

        return parameters
                .stream()
                .filter(e -> Objects.equals(e.getKey(), keyName))
                .mapToInt(e -> getNumericValue(e.getValue()))
                .findFirst()
                .orElse(defaultValue);
    }

    private static int getNumericValue(List<String> values) {
        int number;
        if (values.isEmpty()) {
            number = -1;
        } else {
            var text = values.get(0);
            if (text.chars().allMatch(Character::isDigit)) {
                number = Integer.valueOf(text);
            } else {
                number = -1;
            }
        }
        return number;
    }
}
