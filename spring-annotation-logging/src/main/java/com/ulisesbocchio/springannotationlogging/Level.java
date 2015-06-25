package com.ulisesbocchio.springannotationlogging;

import org.slf4j.Logger;

import java.util.function.BiConsumer;

/**
 * @author Ulises Bocchio
 */
public enum Level {
    ERROR(Logger::error),
    WARN(Logger::warn),
    INFO(Logger::info),
    DEBUG(Logger::debug),
    TRACE(Logger::trace);

    private final BiConsumer<Logger, String> loggerFunction;

    Level(BiConsumer<Logger, String> loggerFunction) {
        this.loggerFunction = loggerFunction;
    }

    protected BiConsumer<Logger, String> getLoggerFunction() {
        return loggerFunction;
    }
}
