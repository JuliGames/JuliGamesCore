package net.juligames.core.api.config;

import org.jetbrains.annotations.ApiStatus;

/**
 * @author Ture Bentzin
 * 10.01.2023
 */
@ApiStatus.Experimental
public interface ConfigWriter {
    /**
     * Writes the data of this {@link ConfigWriter} to the configuration
     *
     * @param configuration the configuration
     * @param keyspace      the keyspace to write data to
     */
    void write(Configuration configuration, String keyspace);
}
