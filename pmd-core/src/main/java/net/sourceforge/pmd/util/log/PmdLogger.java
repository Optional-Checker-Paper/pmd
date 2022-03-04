/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.util.log;

import org.slf4j.event.Level;

import net.sourceforge.pmd.annotation.InternalApi;

/**
 * Logger façade. Can probably be converted to just SLF4J logger in PMD 7.
 *
 * @author Clément Fournier
 */
@InternalApi
public interface PmdLogger {

    boolean isLoggable(Level level);

    void log(Level level, String message, Object... formatArgs);

    void logEx(Level level, String message, Object[] formatArgs, Throwable error);

    default void info(String message, Object... formatArgs) {
        log(Level.INFO, message, formatArgs);
    }

    // todo trace and debug should be on SLF4J logger directly
    default void trace(String message, Object... formatArgs) {
        log(Level.TRACE, message, formatArgs);
    }

    default void debug(String message, Object... formatArgs) {
        log(Level.DEBUG, message, formatArgs);
    }

    default void warning(String message, Object... formatArgs) {
        log(Level.WARN, message, formatArgs);
    }

    default void warningEx(String message, Throwable error) {
        logEx(Level.WARN, message, new Object[0], error);
    }

    default void warningEx(String message, Object[] formatArgs, Throwable error) {
        logEx(Level.WARN, message, formatArgs, error);
    }

    default void error(String message, Object... formatArgs) {
        log(Level.ERROR, message, formatArgs);
    }

    default void errorEx(String message, Throwable error) {
        logEx(Level.ERROR, message, new Object[0], error);
    }

    default void errorEx(String message, Object[] formatArgs, Throwable error) {
        logEx(Level.ERROR, message, formatArgs, error);
    }

    int numErrors();

}
