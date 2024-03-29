package net.juligames.core.config;

import com.hazelcast.core.DistributedObject;
import net.juligames.core.Core;
import net.juligames.core.api.config.ConfigWriter;
import net.juligames.core.api.config.Configuration;
import net.juligames.core.api.config.ConfigurationAPI;
import net.juligames.core.api.config.Interpreter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

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
    public @NotNull CoreConfiguration getOrCreate(@NotNull String name) {
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
    public @NotNull CoreConfiguration getOrCreate(@NotNull Properties defaults) {
        return CoreConfiguration.fromProperties(defaults);
    }

    @Override
    public boolean exists(String name) {
        return Core.getInstance().getHazelDataApi().getAll().stream().anyMatch(distributedObject -> distributedObject.getName().equals(name));
    }

    @Override
    public @NotNull CoreConfiguration master() {
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
    public @NotNull Comparator<? extends Configuration> comparator() {
        return (o1, o2) -> Comparator.<Configuration>naturalOrder().compare(o1, o2);
    }

    @Override
    public @NotNull OfflineConfiguration createNewOfflineConfiguration(@NotNull String name) {
        return new OfflineConfiguration(name);
    }

    @Override
    public @NotNull OfflineConfiguration createNewOfflineConfiguration(@NotNull Properties defaults) {
        return new OfflineConfiguration(defaults);
    }

    @Override
    public @NotNull OfflineConfiguration createNewOfflineConfiguration(@NotNull Map<String, String> defaults) {
        return new OfflineConfiguration(defaults.get("configuration_name"), defaults);
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
    public <T> @NotNull Collection<String> split(@NotNull Collection<T> collection, @NotNull Interpreter<T> interpreter) {
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
    public <T> @NotNull ConfigWriter splitToWriter(@NotNull Collection<T> collection, @NotNull Interpreter<T> interpreter) {
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
    public <T> @NotNull Collection<T> tryReadSplitCollection(@NotNull Configuration configuration, @NotNull String keySpace, @NotNull Interpreter<T> interpreter) {
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
    public <T> @NotNull Collection<T> tryReadSplitCollection(@NotNull Iterable<String> strings, @NotNull Interpreter<T> interpreter) {
        final Collection<T> ts = new ArrayList<>();
        for (String value : strings)
            try {
                ts.add(interpreter.interpret(value));
            } catch (Exception ignored) {
            }
        return ts;
    }

    @Override
    public @NotNull Properties initializeProperties(@NotNull String name, @Nullable String header) {
        final Properties defaults = new Properties();
        defaults.setProperty("configuration_name", name);
        defaults.setProperty("configuration_header", header);
        return defaults;
    }

    @Override
    public @NotNull Configuration merge(@NotNull Configuration c1, @NotNull Configuration c2) {
        c1.copyAndAppendContentTo(c2);
        return c1;
    }

    @Override
    @ApiStatus.Experimental
    public @NotNull Configuration createSectionClone(@NotNull Configuration root, @NotNull String section) {
        return new OfflineConfiguration(section + "@" + root,
                new HashMap<>(root.entrySet().stream()
                        .filter(stringStringEntry -> stringStringEntry.getKey().startsWith(section + "_"))
                        .map(s -> {
                            String after = s.getKey().replaceFirst(section + "_", "");
                            return Map.entry(section + "_" + after.substring(0, after.indexOf("_")), s.getValue());
                        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
    }

    @Override
    public @NotNull @Unmodifiable Collection<Configuration> getAll() {
        return getAllHazels()
                .stream()
                .map(this::getOrCreate)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public @NotNull @Unmodifiable Collection<String> getAllHazels() {
        return Core.getInstance()
                .getHazelDataApi()
                .getAll()
                .stream()
                .map(DistributedObject::getName)
                .filter(s -> s.startsWith("config:"))
                .collect(Collectors.toUnmodifiableSet());
    }
}
