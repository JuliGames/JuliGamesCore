package net.juligames.core.config;

import com.hazelcast.map.IMap;
import de.bentzin.tools.pair.BasicPair;
import net.juligames.core.Core;
import net.juligames.core.api.config.Configuration;
import net.juligames.core.api.config.Interpreter;
import net.juligames.core.api.misc.TriConsumer;
import org.checkerframework.checker.optional.qual.MaybePresent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * @author Ture Bentzin
 * 26.11.2022
 * @implNote RESERVED KEYS: "configuration_header", "configuration_name"
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public class CoreConfiguration implements Configuration {

    private final String name;
    private String header_comment = Core.getFullCoreName() + " :: a default configuration file";
    private IMap<String, String> data;


    public static @NotNull String generateHazel(@NotNull String name, boolean containingPrefix) {
        return containingPrefix ? name : "config:" + name;
    }


    public CoreConfiguration(String name) {
        this.name = name;
        data = hazel();
        assert Objects.equals(getStringOrNull("configuration_name"), name); //just to avoid BNick content in the configurationSystem

        String configuration_header = getStringOrNull("configuration_header");
        if (configuration_header != null) {
            setHeader_comment(configuration_header);
        }
    }

    /**
     * This will not override data in the {@link Map} unless override is set to true
     *
     * @param properties the defaults
     * @return the config
     */
    public static @NotNull CoreConfiguration fromProperties(@NotNull Properties properties, boolean override) {
        String name = properties.getProperty("configuration_name");
        if (name == null) throw new IllegalArgumentException("properties does not specify a valid configuration_name!");
        CoreConfiguration configuration = new CoreConfiguration(name);
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        IMap<String, String> map = configuration.accessHazel().get();
        for (Map.Entry<Object, Object> entry : entries) {
            if (override) {
                String old = map.put(entry.getKey().toString(), entry.getValue().toString()); //oh man... oh man
                Core.getInstance().getCoreLogger().debug("OVERRIDE: set " + entry.getKey() + "from " + old + " to " + entry.getValue());
            } else if (!map.containsKey(entry.getKey().toString())) {
                String old = map.put(entry.getKey().toString(), entry.getValue().toString()); //oh man... oh man
                Core.getInstance().getCoreLogger().debug("set " + entry.getKey() + "from " + old + " to " + entry.getValue());
            }
        }
        configuration.updateHazel();
        return configuration;
    }


    /**
     * This will not override data in the {@link Map}
     *
     * @param properties the defaults
     * @return the config
     */
    public static @NotNull CoreConfiguration fromProperties(@NotNull Properties properties) {
        return fromProperties(properties, false);
    }

    private @NotNull IMap<String, String> hazel() {
        return hazel(false);
    }

    @ApiStatus.Internal
    private @NotNull IMap<String, String> hazel(@SuppressWarnings("SameParameterValue") boolean containingPrefix) {
        return Core.getInstance().getOrThrow().getMap(generateHazel(name, containingPrefix));
    }

    public void updateHazel() {
        data = hazel();
    }

    public @NotNull Map<String, String> export() {
        return new HashMap<>(data);
    }

    @ApiStatus.Internal
    public Supplier<IMap<String, String>> accessHazel() {
        return this::hazel;
    }

    public @NotNull Set<String> keySet() {
        return hazel().keySet();
    }

    public @NotNull Set<Map.Entry<String, String>> entrySet() {
        return hazel().entrySet();
    }

    public @NotNull Collection<String> values() {
        return hazel().values();
    }

    public void writeTo(@NotNull OutputStream dataStream) throws IOException {
        cloneToProperties().store(dataStream, header_comment);
    }

    @Override
    public @NotNull Properties cloneToProperties() {
        final Properties properties = new Properties();
        for (Map.Entry<String, String> config : entrySet()) {
            properties.setProperty(config.getKey(), config.getValue());
        }
        return properties;
    }

    @Override
    public boolean isSet(@NotNull String key) {
        return hazel().containsKey(key);
    }

    @Override
    public boolean isSet(@NotNull Supplier<String> key) {
        return isSet(key.get());
    }

    @Override
    public <T> boolean isSetAndValid(@NotNull String key, @NotNull Interpreter<T> interpreter) {
        try {
            return get(key, interpreter).isPresent();
        } catch (RuntimeException ignored) {
            return false;
        }
    }

    @Override
    public <T> boolean isSetAndValid(@NotNull Supplier<String> key, @NotNull Interpreter<T> interpreter) {
        return isSetAndValid(key.get(), interpreter);
    }

    @Override
    public @NotNull Optional<String> getString(@NotNull String key) {
        @Nullable String extract = data.get(key);
        return Optional.ofNullable(extract);
    }

    @Override
    public @NotNull Optional<String> getString(@NotNull Supplier<String> key) {
        return getString(key.get());
    }

    @Override
    public @Nullable String getStringOrNull(@NotNull String key) {
        @Nullable String extract = data.get(key);
        return extract;
    }

    @Override
    public @Nullable String getStringOrNull(@NotNull Supplier<String> key) {
        return getStringOrNull(key.get());
    }

    @Override
    public @NotNull Optional<Integer> getInteger(@NotNull String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.valueOf(extract));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull Optional<Integer> getInteger(@NotNull Supplier<String> key) {
        return getInteger(key.get());
    }

    @Override
    public @Nullable Integer getIntegerOrNull(@NotNull String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return null;
        }
        try {
            return Integer.valueOf(extract);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public @Nullable Integer getIntegerOrNull(@NotNull Supplier<String> key) {
        return getIntegerOrNull(key.get());
    }

    @Override
    public @NotNull Optional<Double> getDouble(@NotNull String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Double.valueOf(extract));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull Optional<Double> getDouble(@NotNull Supplier<String> key) {
        return getDouble(key.get());
    }

    @Override
    public @Nullable Double getDoubleOrNull(@NotNull String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return null;
        }
        try {
            return Double.parseDouble(extract);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public @Nullable Double getDoubleOrNull(@NotNull Supplier<String> key) {
        return getDoubleOrNull(key.get());
    }

    @Override
    public @NotNull Optional<Long> getLong(@NotNull String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Long.parseLong(extract));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull Optional<Long> getLong(@NotNull Supplier<String> key) {
        return getLong(key.get());
    }

    @Override
    public @Nullable Long getLongOrNull(@NotNull String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return null;
        }
        try {
            return Long.parseLong(extract);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public @Nullable Long getLongOrNull(@NotNull Supplier<String> key) {
        return getLongOrNull(key.get());
    }

    @Override
    public @NotNull Optional<Short> getShort(@NotNull String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Short.parseShort(extract));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull Optional<Short> getShort(@NotNull Supplier<String> key) {
        return getShort(key.get());
    }

    @Override
    public @Nullable Short getShortOrNull(@NotNull String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return null;
        }
        try {
            return Short.parseShort(extract);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public @Nullable Short getShortOrNull(@NotNull Supplier<String> key) {
        return getShortOrNull(key.get());
    }

    @Override
    public @NotNull Optional<Byte> getByte(@NotNull String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Byte.parseByte(extract));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull Optional<Byte> getByte(@NotNull Supplier<String> key) {
        return getByte(key.get());
    }

    @Override
    public @Nullable Byte getByteOrNull(@NotNull String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return null;
        }
        try {
            return Byte.parseByte(extract);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public @Nullable Byte getByteOrNull(@NotNull Supplier<String> key) {
        return getByteOrNull(key.get());
    }

    @Override
    public @NotNull Optional<Boolean> getBoolean(@NotNull String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Boolean.parseBoolean(extract));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull Optional<Boolean> getBoolean(@NotNull Supplier<String> key) {
        return getBoolean(key.get());
    }

    @Override
    public @Nullable Boolean getBooleanOrNull(@NotNull String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return null;
        }
        try {
            return Boolean.parseBoolean(extract);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public @Nullable Boolean getBooleanOrNull(@NotNull Supplier<String> key) {
        return getBooleanOrNull(key.get());
    }

    @Override
    public @NotNull Optional<Float> getFloat(@NotNull String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Float.parseFloat(extract));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull Optional<Float> getFloat(@NotNull Supplier<String> key) {
        return getFloat(key.get());
    }

    @Override
    public @Nullable Float getFloatOrNull(@NotNull String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return null;
        }
        try {
            return Float.parseFloat(extract);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public @Nullable Float getFloatOrNull(@NotNull Supplier<String> key) {
        return getFloatOrNull(key.get());
    }

    @Override
    public <T> @NotNull Collection<T> getCollection(@NotNull String keyspace, @NotNull Interpreter<T> interpreter) {
        return IterableSplitter.tryReadSplitCollection(this, keyspace, interpreter);
    }

    @Override
    public <T> @NotNull Collection<T> getCollection(@NotNull Supplier<String> keyspace, @NotNull Interpreter<T> interpreter) {
        return getCollection(keyspace.get(), interpreter);
    }

    //legacy interpreter
    @Override
    public @MaybePresent <T> Optional<T> get(@NotNull String key, @NotNull Function<String, T> interpreter) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(interpreter.apply(extract));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //legacy interpreter
    @Override
    public @MaybePresent <T> Optional<T> get(@NotNull Supplier<String> key, @NotNull Function<String, T> interpreter) {
        return get(key.get(), interpreter);
    }

    //legacy interpreter
    @Override
    public <T> @Nullable T getOrNull(@NotNull String key, @NotNull Function<String, T> interpreter) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return null;
        }
        try {
            return interpreter.apply(extract);
        } catch (Exception e) {
            return null;
        }
    }

    //legacy interpreter
    @Override
    public <T> @Nullable T getOrNull(@NotNull Supplier<String> key, @NotNull Function<String, T> interpreter) {
        return getOrNull(key.get(), interpreter);
    }

    @Override
    public @MaybePresent <T> Optional<T> get(@NotNull String key, @NotNull Interpreter<T> interpreter) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(interpreter.interpret(extract));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @MaybePresent <T> Optional<T> get(@NotNull Supplier<String> key, @NotNull Interpreter<T> interpreter) {
        return get(key.get(), interpreter);
    }

    @Override
    public <T> @Nullable T getOrNull(@NotNull String key, @NotNull Interpreter<T> interpreter) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return null;
        }
        try {
            return interpreter.interpret(extract);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public <T> @Nullable T getOrNull(@NotNull Supplier<String> key, @NotNull Interpreter<T> interpreter) {
        return getOrNull(key.get(), interpreter);
    }

    @Override
    public void setString(@NotNull String key, @NotNull String value) {
        data.put(key, value);
    }

    @Override
    public void setString(@NotNull String key, @NotNull Supplier<String> value) {
        setString(key, value.get());
    }

    @Override
    public void setInteger(@NotNull String key, @NotNull Integer value) {
        data.put(key, value.toString());
    }

    @Override
    public void setInteger(@NotNull String key, @NotNull Supplier<Integer> value) {
        setInteger(key, value.get());
    }

    @Override
    public void setDouble(@NotNull String key, @NotNull Double value) {
        data.put(key, value.toString());
    }

    @Override
    public void setDouble(@NotNull String key, @NotNull Supplier<Double> value) {
        setDouble(key, value.get());
    }

    @Override
    public void setLong(@NotNull String key, @NotNull Long value) {
        data.put(key, value.toString());
    }

    @Override
    public void setLong(@NotNull String key, @NotNull Supplier<Long> value) {
        setLong(key, value.get());
    }

    @Override
    public void setShort(@NotNull String key, @NotNull Short value) {
        data.put(key, value.toString());
    }

    @Override
    public void setShort(@NotNull String key, @NotNull Supplier<Short> value) {
        setShort(key, value.get());
    }

    @Override
    public void setByte(@NotNull String key, @NotNull Byte value) {
        data.put(key, value.toString());
    }

    @Override
    public void setByte(@NotNull String key, @NotNull Supplier<Byte> value) {
        setByte(key, value.get());
    }

    @Override
    public void setBoolean(@NotNull String key, @NotNull Boolean value) {
        data.put(key, value.toString());
    }

    @Override
    public void setBoolean(@NotNull String key, @NotNull Supplier<Boolean> value) {
        setBoolean(key, value.get());
    }

    @Override
    public void setFloat(@NotNull String key, @NotNull Float value) {
        data.put(key, value.toString());
    }

    @Override
    public void setFloat(@NotNull String key, @NotNull Supplier<Float> value) {
        setFloat(key, value.get());
    }

    @Override
    public <T> void set(@NotNull String key, @NotNull T value, @NotNull Interpreter<T> interpreter) {
        data.put(key, interpreter.reverse(value));
    }

    @Override
    public <T> void set(@NotNull String key, @NotNull Supplier<T> value, @NotNull Interpreter<T> interpreter) {
        set(key, value.get(), interpreter);
    }

    @Override
    public <T> void set(@NotNull Supplier<String> key, @NotNull T value, @NotNull Interpreter<T> interpreter) {
        set(key.get(), value, interpreter);
    }

    @Override
    public <T> void set(@NotNull Supplier<String> key, @NotNull Supplier<T> value, @NotNull Interpreter<T> interpreter) {
        set(key.get(), value.get(), interpreter);
    }

    @Override
    public <T> void setIterable(@NotNull String keySpace, @NotNull Iterable<T> iterable, @NotNull Interpreter<T> interpreter) {
        IterableSplitter.splitAndWrite(iterable, interpreter, this, keySpace);
    }

    @Override
    public <T> void setIterable(@NotNull Supplier<String> keySpace, @NotNull Iterable<T> iterable, @NotNull Interpreter<T> interpreter) {
        setIterable(keySpace.get(), iterable, interpreter);
    }

    @Override
    public <T> void setIterable(@NotNull String keySpace, @NotNull Supplier<Iterable<T>> iterable, @NotNull Interpreter<T> interpreter) {
        setIterable(keySpace, iterable.get(), interpreter);
    }

    @Override
    public <T> void setIterable(@NotNull Supplier<String> keySpace, @NotNull Supplier<Iterable<T>> iterable, @NotNull Interpreter<T> interpreter) {
        setIterable(keySpace, iterable.get(), interpreter);
    }

    @Override
    public void del(@NotNull String key) {
        data.remove(key);
    }

    @Override
    public void del(@NotNull Supplier<String> key) {
        del(key.get());
    }

    @Override
    public void del(String @NotNull ... key) {
        dellAllFromCollection(List.of(key));
    }

    @Override
    public void delAll(@NotNull Supplier<Collection<String>> keys) {
        Collection<String> collection = keys.get();
        dellAllFromCollection(collection);
    }

    @Override
    public void delRecursive(@NotNull String key) {
        data.removeAll(mapEntry -> mapEntry.getKey().startsWith(key));
    }

    @Override
    public void delRecursive(@NotNull Supplier<String> key) {
        delRecursive(key.get());
    }

    @Override
    public void delRecursive(String @NotNull ... keys) {
        delAllRecursive(() -> List.of(keys));
    }

    @Override
    public void delAllRecursive(@NotNull Supplier<Collection<String>> keys) {
        for (String s : keys.get()) {
            delRecursive(s);
        }
    }

    public void dellAllFromCollection(Collection<String> keys) {
        data.removeAll(mapEntry -> keys.contains(mapEntry.getKey()));
    }


    //query's


    @Override
    public <T> T query(@NotNull Function<Supplier<String>, T> query, @NotNull String input) {
        updateHazel();
        return query.apply(() -> input);
    }

    @Override
    public <T> T query(@NotNull BiFunction<Supplier<String>, Interpreter<T>, T> query, @NotNull String input, @NotNull Interpreter<T> interpreter) {
        updateHazel();
        return query.apply(() -> input, interpreter);
    }

    @Override
    public <T> void query(@NotNull BiConsumer<String, Supplier<T>> query, @NotNull String input, @NotNull Supplier<T> t) {
        updateHazel();
        query.accept(input, t);
    }

    @Override
    public <T> void query(@NotNull TriConsumer<String, T, Supplier<Interpreter<T>>> query, @NotNull String input, @NotNull Interpreter<T> interpreter, @NotNull Supplier<T> t) {
        updateHazel();
        query.consume(input, t.get(), () -> interpreter);
    }

    @Override
    public void feed(@NotNull String key, @NotNull Consumer<String> rawConsumer) {
        rawConsumer.accept(getStringOrNull(key));
    }

    public String header_comment() {
        return header_comment;
    }

    public void setHeader_comment(String header_comment) {
        this.header_comment = header_comment;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public void copyAndAppendContentTo(@NotNull Configuration configuration) {
        IMap<String, String> hazel = hazel();
        Set<String> keySet = configuration.keySet();
        for (Map.Entry<String, String> stringStringEntry : hazel) {
            if (!keySet.contains(stringStringEntry.getKey()))
                configuration.setString(stringStringEntry.getKey(), stringStringEntry.getValue());
        }
    }

    @Override
    public void copyAndOverrideContentTo(@NotNull Configuration configuration) {
        hazel().forEach(configuration::setString);
    }

    @Override
    public @NotNull Configuration copyToOffline() {
        return copyToOffline(getName());
    }

    @Override
    public @NotNull Configuration copy(@NotNull String name) {
        CoreConfiguration configuration = Core.getInstance().getConfigurationApi().getOrCreate(name);
        configuration.clear();
        copyAndOverrideContentTo(configuration);
        return configuration;
    }

    @Override
    public @NotNull OfflineConfiguration copyToOffline(@NotNull String name) {
        OfflineConfiguration offlineConfiguration = new OfflineConfiguration(name);
        copyAndOverrideContentTo(offlineConfiguration);
        return offlineConfiguration;
    }

    @Override
    public void appendAll(@NotNull Collection<Configuration> configurations) {
        for (Configuration configuration : configurations) {
            configuration.copyAndAppendContentTo(this);
        }
    }

    @Override
    public <R> R doWithData(@NotNull Function<Map<String, String>, R> function) {
        return function.apply(hazel());
    }

    @Override
    public void set(@NotNull BasicPair<String, String> basicPair) {
        setString(basicPair.getFirst(), basicPair.getSecond());
    }


    //ifAbsent

    @Override
    public void setStringIfAbsent(@NotNull String key, @NotNull String value) {
        if (isSet(key))
            setString(key, value);
    }

    @Override
    public void setStringIfAbsent(@NotNull String key, @NotNull Supplier<String> value) {
        if (isSet(key))
            setString(key, value);
    }

    @Override
    public void setIntegerIfAbsent(@NotNull String key, @NotNull Integer value) {
        if (isSet(key))
            setInteger(key, value);
    }

    @Override
    public void setIntegerIfAbsent(@NotNull String key, @NotNull Supplier<Integer> value) {
        if (isSet(key))
            setInteger(key, value);
    }

    @Override
    public void setDoubleIfAbsent(@NotNull String key, @NotNull Double value) {
        if (isSet(key))
            setDouble(key, value);
    }

    @Override
    public void setDoubleIfAbsent(@NotNull String key, @NotNull Supplier<Double> value) {
        if (isSet(key))
            setDouble(key, value);
    }

    @Override
    public void setLongIfAbsent(@NotNull String key, @NotNull Long value) {
        if (isSet(key))
            setLong(key, value);
    }

    @Override
    public void setLongIfAbsent(@NotNull String key, @NotNull Supplier<Long> value) {
        if (isSet(key))
            setLong(key, value);
    }

    @Override
    public void setShortIfAbsent(@NotNull String key, @NotNull Short value) {
        if (isSet(key))
            setShort(key, value);
    }

    @Override
    public void setShortIfAbsent(@NotNull String key, @NotNull Supplier<Short> value) {
        if (isSet(key))
            setShort(key, value);
    }

    @Override
    public void setByteIfAbsent(@NotNull String key, @NotNull Byte value) {
        if (isSet(key))
            setByte(key, value);
    }

    @Override
    public void setByteIfAbsent(@NotNull String key, @NotNull Supplier<Byte> value) {
        if (isSet(key))
            setByte(key, value);
    }

    @Override
    public void setBooleanIfAbsent(@NotNull String key, @NotNull Boolean value) {
        if (isSet(key))
            setBoolean(key, value);
    }

    @Override
    public void setBooleanIfAbsent(@NotNull String key, @NotNull Supplier<Boolean> value) {
        if (isSet(key))
            setBoolean(key, value);
    }

    @Override
    public void setFloatIfAbsent(@NotNull String key, @NotNull Float value) {
        if (isSet(key))
            setFloat(key, value);
    }

    @Override
    public void setFloatIfAbsent(@NotNull String key, @NotNull Supplier<Float> value) {
        if (isSet(key))
            setFloat(key, value);
    }

    @Override
    public <T> void setIfAbsent(@NotNull String key, @NotNull T value, @NotNull Interpreter<T> interpreter) {
        if (isSet(key))
            set(key, value, interpreter);
    }

    @Override
    public <T> void setIfAbsent(@NotNull String key, @NotNull Supplier<T> value, @NotNull Interpreter<T> interpreter) {
        if (isSet(key))
            set(key, value, interpreter);
    }

    @Override
    public <T> void setIfAbsent(@NotNull Supplier<String> key, @NotNull T value, @NotNull Interpreter<T> interpreter) {
        if (isSet(key))
            set(key, value, interpreter);
    }

    @Override
    public <T> void setIfAbsent(@NotNull Supplier<String> key, @NotNull Supplier<T> value, @NotNull Interpreter<T> interpreter) {
        if (isSet(key))
            set(key, value, interpreter);
    }


    @Override
    public void setIfAbsent(@NotNull BasicPair<String, String> basicPair) {
        if (isSet(basicPair.getFirst()))
            set(basicPair);
    }


    //continue

    @Override
    public void doWithData(@NotNull Consumer<Map<String, String>> action) {
        action.accept(hazel());
    }

    public final @NotNull String getConjoinedDescription() {
        return getName() + "\n" + header_comment;
    }

    /**
     * Changes will take effect after regeneration of this object!!!
     *
     * @param newName the new name
     */
    public void updateName(String newName) {
        setString("configuration_name", newName);
    }

    @ApiStatus.Internal
    private <T> @NotNull List<T> collectIterable(@NotNull Iterable<T> iterable) {
        List<T> result = new ArrayList<>();
        iterable.forEach(result::add);
        return result;
    }

    @Override
    public void applyDefaults(@NotNull Properties defaults) {
        for (Map.Entry<Object, Object> objectObjectEntry : defaults.entrySet()) {
            final String key = objectObjectEntry.getKey().toString(), value = objectObjectEntry.getValue().toString();
            hazel().putIfAbsent(key, value);
        }
    }

    @Override
    public String toString() {
        return name;
    }

    @ApiStatus.Internal
    private void clear() {
        hazel().clear();
    }

    @Override
    public @NotNull Stream<String> searchValue(@NotNull String value) {
        Predicate<Map.Entry<String, String>> filter = stringStringEntry -> stringStringEntry.getValue().equals(value);
        return entrySet().stream().filter(filter).map(Map.Entry::getKey);
    }
}
