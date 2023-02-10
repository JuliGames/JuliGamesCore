package net.juligames.core.util;

import de.bentzin.tools.logging.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Ture Bentzin
 * 22.01.2023
 * @apiNote Maybe moved to DevTools (de.bentzin.tools.logging)
 */
@SuppressWarnings("unused")
public class ConsumingLogger extends Logger {

    private final BiConsumer<String, LogLevel> logger;

    public ConsumingLogger(String name, Logger parent, BiConsumer<String, LogLevel> logger) {
        super(name, parent);
        this.logger = logger;
    }

    public ConsumingLogger(String name, BiConsumer<String, LogLevel> logger) {
        super(name);
        this.logger = logger;
    }

    public ConsumingLogger(String name, Logger parent, Consumer<String> logger) {
        super(name, parent);
        this.logger = (s, logLevel) -> logger.accept(s);
    }

    public ConsumingLogger(String name, Consumer<String> logger) {
        super(name);
        this.logger = (s, logLevel) -> logger.accept(s);
    }

    @Contract(" -> new")
    public static @NotNull ConsumingLogger generateFallbackLogger() {
        return new ConsumingLogger("fallback", FALLBACK_LOGGER);
    }

    @Override
    public void log(String message, LogLevel logLevel) {
        logger.accept(message, logLevel);
    }

    @Override
    public ConsumingLogger adopt(String name) {
        return new ConsumingLogger(name, this, logger);
    }
}
