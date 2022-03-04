/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.util.log;

import org.slf4j.Logger;
import org.slf4j.event.Level;

import net.sourceforge.pmd.annotation.InternalApi;

/**
 * A {@link Logger} (java.util) based logger impl.
 *
 * @author Clément Fournier
 */
@InternalApi
public class SimplePmdLogger extends PmdLoggerBase implements PmdLogger {

    private final Logger backend;

    public SimplePmdLogger(Logger backend) {
        this.backend = backend;
    }

    @Override
    protected boolean isLoggableImpl(Level level) {
        return backend.isEnabledForLevel(level);
    }

    @Override
    protected void logImpl(Level level, String message, Object[] formatArgs) {
        backend.atLevel(level).log(message, formatArgs);
    }
}
