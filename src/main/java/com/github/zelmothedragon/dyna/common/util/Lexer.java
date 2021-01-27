package com.github.zelmothedragon.dyna.common.util;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FunctionalInterface
public interface Lexer<T> {

    Optional<T> tryParse(String text);

    default <R> Lexer<R> map(final Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        return t -> this.tryParse(t).map(mapper);
    }

    default Lexer<T> or(final Lexer<? extends T> other) {
        Objects.requireNonNull(other);
        return t -> tryParse(t)
                .or(() -> other.tryParse(t));
    }

    default Lexer<T> with(final String regex, final Function<? super String, ? extends T> mapper) {
        return or(from(regex).map(mapper));
    }

    static <T> Lexer<T> of() {
        return t -> {
            Objects.requireNonNull(t);
            return Optional.empty();
        };
    }

    static Lexer<String> from(final String regex) {
        Objects.requireNonNull(regex);
        var pattern = Pattern.compile(regex);
        return from(pattern);
    }

    static Lexer<String> from(final Pattern pattern) {
        Objects.requireNonNull(pattern);
        requireOneGroup(pattern);
        return t -> {
            Objects.requireNonNull(t);
            return Optional
                    .of(pattern.matcher(t))
                    .filter(Matcher::matches)
                    .map(m -> m.group(1));
        };
    }

    static <T> Lexer<T> from(
            final List<String> regexes,
            final List<Function<? super String, ? extends T>> mappers) {

        Objects.requireNonNull(regexes);
        Objects.requireNonNull(mappers);
        regexes.forEach(r -> requireOneGroup(Pattern.compile(r)));
        if (regexes.size() != mappers.size()) {
            throw new IllegalArgumentException();
        }

        return createLexerFromLists(
                List.copyOf(regexes),
                List.copyOf(mappers)
        );
    }

    private static void requireOneGroup(final Pattern pattern) {
        if (pattern.matcher("").groupCount() != 1) {
            throw new IllegalArgumentException();
        }
    }

    private static <T> Lexer<T> createLexerFromLists(
            final List<String> regexes,
            final List<Function<? super String, ? extends T>> mappers) {

        return t -> {
            Objects.requireNonNull(t);
            Optional<T> result = null;
            if (regexes.isEmpty()) {
                result = Optional.empty();
            } else if (mappers.isEmpty()) {
                result = Optional.empty();
            } else {
                var matcher = Pattern
                        .compile(String.join("|", regexes))
                        .matcher(t);
                if (!matcher.matches()) {
                    result = Optional.empty();
                } else {
                    for (var i = 0; i < matcher.groupCount(); i++) {
                        var group = matcher.group(i + 1);
                        if (Objects.nonNull(group)) {
                            result = Optional.of(group).map(mappers.get(i));
                        }
                    }

                }
            }
            return result;
        };
    }

}
