package net.juligames.core.api.config;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Comparator;
import java.util.Properties;

/**
 * @author Ture Bentzin
 * 26.11.2022
 */
public interface ConfigurationAPI {

    /**
     * This will create a new configuration (if none with the same name is already present)
     *
     * @param name the name
     * @return a new Configuration or the old if already one existed
     */
    Configuration getOrCreate(String name);

    /**
     * This will create a new configuration based of the given defaults
     *
     * @param defaults the defaults to set
     * @return a new Configuration or the old if already one existed
     */
    Configuration getOrCreate(Properties defaults);

    Configuration master();

    Comparator<? extends Configuration> comparator();

    //collections on demand - 1.1

    /**
     * This will split the given collection using the given interpreter
     *
     * @param collection  the collection
     * @param interpreter the interpreter
     * @param <T>         Type
     * @return a {@link Collection} out of the interpreted Ts
     */
    <T> Collection<String> split(Collection<T> collection, Interpreter<T> interpreter);

    /**
     * This will split the given {@link Collection} and return a writer that will
     *
     * @param collection  the collection to spilt
     * @param interpreter the interpreter to use
     * @param <T>         Type
     * @return a {@link ConfigWriter} ready to write the split data
     */
    <T> ConfigWriter splitToWriter(Collection<T> collection, Interpreter<T> interpreter);

    /**
     * This will split and write the Collection using {@link #splitToWriter(Collection, Interpreter)}
     *
     * @param collection    the collection
     * @param interpreter   the interpreter
     * @param configuration the configuration
     * @param keySpace      the (empty) keyspace
     * @param <T>           type
     * @deprecated moved to {@link Configuration#setIterable(String, Iterable, Interpreter)}
     */
    @Deprecated
    default <T> void spiltAndWrite(Collection<T> collection, Interpreter<T> interpreter, Configuration configuration, String keySpace) {
        splitToWriter(collection, interpreter).write(configuration, keySpace);
    }

    /**
     * This will try to read a {@link Collection} that was split by {@link #splitToWriter(Collection, Interpreter)}
     *
     * @param keySpace    the keyspace
     * @param interpreter the interpreter
     * @param <T>         Type
     * @return a {@link java.util.Collection} with all Ts that where read successfully
     * @deprecated Moved to {@link Configuration#getCollection(String, Interpreter)}
     */
    @Deprecated
    <T> Collection<T> tryReadSplitCollection(Configuration configuration, String keySpace, Interpreter<T> interpreter);


    /**
     * This will try to read a {@link Collection} that was split by {@link #splitToWriter(Collection, Interpreter)}
     *
     * @param strings     the raw values
     * @param interpreter the interpreter
     * @param <T>         Type
     * @return a {@link java.util.Collection} with all Ts that where read successfully
     */
    @ApiStatus.Experimental
    <T> Collection<T> tryReadSplitCollection(Iterable<String> strings, Interpreter<T> interpreter);


    /**
     * @param name   stored as configuration_name
     * @param header stored as configuration_header
     * @return initialized Properties to be used with the {@link ConfigurationAPI}
     */
    Properties initializeProperties(@NotNull String name, @Nullable String header);
}
