package net.juligames.core.config;

import net.juligames.core.api.config.ConfigWriter;
import net.juligames.core.api.config.Configuration;
import net.juligames.core.api.config.ConfigurationAPI;
import net.juligames.core.api.config.Interpreter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ture Bentzin
 * 26.11.2022
 */
@ApiStatus.Internal
public class CoreConfigurationApi implements ConfigurationAPI {

    public static final String MASTER_CONFIG_NAME = "master-config";
    public static final String DATABASE_CONFIG_NAME = "database";

    @Override
    public Configuration getOrCreate(String name) {
        return new CoreConfiguration(name);
    }

    /**
     * This will create a new configuration (if none with the same name is already present)
     *
     * @param defaults the defaults
     * @return a new Configuration or the old if already one existed
     * @apiNote configuration_name is the reserved key for the name!!
     */
    @Override
    public Configuration getOrCreate(Properties defaults) {
        return CoreConfiguration.fromProperties(defaults);
    }

    @Override
    public Configuration master() {
        return getOrCreate(MASTER_CONFIG_NAME);
    }

    @ApiStatus.Internal
    public Configuration database() {
        Properties properties = new Properties();
        properties.setProperty("configuration_name", "database");
        properties.setProperty("jdbc", "jdbc:mysql://root@localhost:3306/minecraft");
        return getOrCreate(properties);
    }

    /**
     * This comparator compares the configurations by size
     */
    @Override
    public Comparator<? extends Configuration> comparator() {
        return (o1, o2) -> Comparator.<Configuration>naturalOrder().compare(o1, o2);
    }

    /**
     * This will split the given collection using the given interpreter
     *
     * @param collection  the collection
     * @param interpreter the interpreter
     * @param <T>         Type
     * @return a {@link Collection} out of the interpreted Ts
     */
    @Override
    public <T> Collection<String> split(Collection<T> collection, Interpreter<T> interpreter) {
        return IterableSplitter.simpleSplit(collection, interpreter);
    }

    /**
     * This will split the given {@link Collection} and return a writer that will
     *
     * @param collection  the collection to spilt
     * @param interpreter the interpreter to use
     * @param <T>         Type
     * @return a {@link ConfigWriter} ready to write the split data
     */
    @Override
    public <T> ConfigWriter splitToWriter(Collection<T> collection, Interpreter<T> interpreter) {
        return IterableSplitter.splitToWriter(collection, interpreter);
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
    @Override
    public <T> Collection<T> tryReadSplitCollection(@NotNull Configuration configuration, String keySpace, Interpreter<T> interpreter) {
        return tryReadSplitCollection(configuration.entrySet().stream().filter(entry ->
                        entry.getKey().startsWith(keySpace)).map(Map.Entry::getValue)
                .collect(Collectors.toUnmodifiableSet()), interpreter);
    }

    /**
     * This will try to read a {@link Collection} that was split by {@link #splitToWriter(Collection, Interpreter)}
     *
     * @param strings     the raw values
     * @param interpreter the interpreter
     * @param <T>         Type
     * @return a {@link java.util.Collection} with all Ts that where read successfully
     */
    @Override
    public <T> Collection<T> tryReadSplitCollection(@NotNull Iterable<String> strings, Interpreter<T> interpreter) {
        final Collection<T> ts = new ArrayList<>();
        for (String value : strings)
            try {
                ts.add(interpreter.interpret(value));
            } catch (Exception ignored) {
            }
        return ts;
    }

    @Override
    public Properties initializeProperties(@NotNull String name, @Nullable String header) {
        final Properties defaults = new Properties();
        defaults.setProperty("configuration_name", name);
        defaults.setProperty("configuration_header", header);
        return defaults;
    }
}
