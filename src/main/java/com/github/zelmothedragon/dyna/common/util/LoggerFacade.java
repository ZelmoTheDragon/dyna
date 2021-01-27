package com.github.zelmothedragon.dyna.common.util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public final class LoggerFacade {

    private static final String KEY_METHOD_NAME = "methodName";

    private static final String KEY_LINE_NUMBER = "lineNumber";

    private LoggerFacade() {
        throw new UnsupportedOperationException("Instance not allowed");
    }

    public static void trace(final Supplier<String> message) {
        Logger logger = getLogger();
        doLog(logger, Logger::isTraceEnabled, logger::trace, message);
    }

    public static void trace(final Supplier<String> message, final Object parameter) {
        Logger logger = getLogger();
        doLog(logger, Logger::isTraceEnabled, logger::trace, message, Arrays.asList(parameter));
    }

    public static void trace(final Supplier<String> message, final List<Object> parameters) {
        Logger logger = getLogger();
        doLog(logger, Logger::isTraceEnabled, logger::trace, message, parameters);
    }

    public static void trace(final Supplier<String> message, final Throwable error) {
        Logger logger = getLogger();
        doLog(logger, Logger::isTraceEnabled, logger::trace, message, error);
    }

    public static void debug(final Supplier<String> message) {
        Logger logger = getLogger();
        doLog(logger, Logger::isDebugEnabled, logger::debug, message);
    }

    public static void debug(final Supplier<String> message, final Object parameter) {
        Logger logger = getLogger();
        doLog(logger, Logger::isDebugEnabled, logger::debug, message, Arrays.asList(parameter));
    }

    public static void debug(final Supplier<String> message, final List<Object> parameters) {
        Logger logger = getLogger();
        doLog(logger, Logger::isDebugEnabled, logger::debug, message, parameters);
    }

    public static void debug(final Supplier<String> message, final Throwable error) {
        Logger logger = getLogger();
        doLog(logger, Logger::isDebugEnabled, logger::debug, message, error);
    }

    public static void info(final Supplier<String> message) {
        Logger logger = getLogger();
        doLog(logger, Logger::isInfoEnabled, logger::info, message);
    }

    public static void info(final Supplier<String> message, final Object parameter) {
        Logger logger = getLogger();
        doLog(logger, Logger::isInfoEnabled, logger::info, message, Arrays.asList(parameter));
    }

    public static void info(final Supplier<String> message, final List<Object> parameters) {
        Logger logger = getLogger();
        doLog(logger, Logger::isInfoEnabled, logger::info, message, parameters);
    }

    public static void info(final Supplier<String> message, final Throwable error) {
        Logger logger = getLogger();
        doLog(logger, Logger::isInfoEnabled, logger::info, message, error);
    }

    public static void warn(final Supplier<String> message) {
        Logger logger = getLogger();
        doLog(logger, Logger::isWarnEnabled, logger::warn, message);
    }

    public static void warn(final Supplier<String> message, final Object parameter) {
        Logger logger = getLogger();
        doLog(logger, Logger::isWarnEnabled, logger::warn, message, Arrays.asList(parameter));
    }

    public static void warn(final Supplier<String> message, final List<Object> parameters) {
        Logger logger = getLogger();
        doLog(logger, Logger::isWarnEnabled, logger::warn, message, parameters);
    }

    public static void warn(final Supplier<String> message, final Throwable error) {
        Logger logger = getLogger();
        doLog(logger, Logger::isWarnEnabled, logger::warn, message, error);
    }

    public static void error(final Supplier<String> message) {
        Logger logger = getLogger();
        doLog(logger, Logger::isErrorEnabled, logger::error, message);
    }

    public static void error(final Supplier<String> message, final Object parameter) {
        Logger logger = getLogger();
        doLog(logger, Logger::isErrorEnabled, logger::error, message, Arrays.asList(parameter));
    }

    public static void error(final Supplier<String> message, final List<Object> parameters) {
        Logger logger = getLogger();
        doLog(logger, Logger::isErrorEnabled, logger::error, message, parameters);
    }

    public static void error(final Supplier<String> message, final Throwable error) {
        Logger logger = getLogger();
        doLog(logger, Logger::isErrorEnabled, logger::error, message, error);
    }

    private static Logger getLogger() {
        StackTraceElement element = Stream
                .of(Thread.currentThread().getStackTrace())
                .filter(e -> !Objects.equals(e.getClassName(), Thread.class.getName()))
                .filter(e -> !Objects.equals(e.getClassName(), LoggerFacade.class.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Caller class not found"));

        String name = element.getClassName();
        MDC.put(KEY_METHOD_NAME, element.getMethodName());
        MDC.put(KEY_LINE_NUMBER, Integer.toString(element.getLineNumber()));
        return LoggerFactory.getLogger(name);
    }

    private static void clearLoggerContext() {
        MDC.remove(KEY_METHOD_NAME);
        MDC.remove(KEY_LINE_NUMBER);
    }

    private static void doLog(
            final Logger logger,
            final Predicate<Logger> enabled,
            final Consumer<String> consumer,
            final Supplier<String> message) {

        if (enabled.test(logger)) {
            consumer.accept(message.get());
        }
        clearLoggerContext();
    }

    private static void doLog(
            final Logger logger,
            final Predicate<Logger> enabled,
            final BiConsumer<String, Object[]> consumer,
            final Supplier<String> message,
            final List<Object> parameters) {

        if (enabled.test(logger)) {
            consumer.accept(message.get(), parameters.toArray());
        }
        clearLoggerContext();
    }

    private static void doLog(
            final Logger logger,
            final Predicate<Logger> enabled,
            final BiConsumer<String, Throwable> consumer,
            final Supplier<String> message,
            final Throwable error) {

        if (enabled.test(logger)) {
            consumer.accept(message.get(), error);
        }
        clearLoggerContext();
    }

}
