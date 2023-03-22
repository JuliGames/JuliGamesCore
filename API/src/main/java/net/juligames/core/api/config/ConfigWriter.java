package net.juligames.core.api.config;

import org.jetbrains.annotations.NotNull;

public interface ConfigWriter {
    /**
     * Writes the data of this {@link ConfigWriter} to the configuration
     *
     * @param configuration the configuration
     * @param keyspace      the keyspace to write data to
     */
    void write(@NotNull Configuration configuration, @NotNull String keyspace);
}
