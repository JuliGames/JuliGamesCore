package net.juligames.core.config;

import com.hazelcast.map.IMap;
import net.juligames.core.Core;
import net.juligames.core.api.TODO;
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
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Ture Bentzin
 * 26.11.2022
 * @implNote RESERVED KEYS: "configuration_header", "configuration_name"
 */
@TODO(doNotcall = true)
public class CoreConfiguration implements Configuration {

    private final String name;
    private String header_comment = Core.getFullCoreName() + " :: a default configuration file";
    private IMap<String, String> data;//May be removed


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
        CoreConfiguration configuration = new CoreConfiguration(name);
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        IMap<String, String> map = configuration.accessHazel().get();
        for (Map.Entry<Object, Object> entry : entries) {
            if (override)
                map.put(entry.getKey().toString(), entry.getValue().toString()); //oh man... oh man
            else if (!map.containsKey(entry.getKey().toString())) {
                map.put(entry.getKey().toString(), entry.getValue().toString()); //oh man... oh man
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
    private @NotNull IMap<String, String> hazel(boolean containingPrefix) {
        return Core.getInstance().getOrThrow().getMap(containingPrefix ? name : "config:" + name);
    }

    public void updateHazel() {
        data = hazel();
    }

    public Map<String, String> export() {
        return new HashMap<>(data);
    }

    @ApiStatus.Internal
    public Supplier<IMap<String, String>> accessHazel() {
        return this::hazel;
    }

    public Set<String> keySet() {
        return hazel().keySet();
    }

    public Set<Map.Entry<String, String>> entrySet() {
        return hazel().entrySet();
    }

    public Collection<String> values() {
        return hazel().values();
    }

    public void writeTo(OutputStream dataStream) throws IOException {
        cloneToProperties().store(dataStream, header_comment);
    }

    @Override
    public Properties cloneToProperties() {
        final Properties properties = new Properties();
        for (Map.Entry<String, String> config : entrySet()) {
            properties.setProperty(config.getKey(), config.getValue());
        }
        return properties;
    }

    @Override
    public Optional<String> getString(String key) {
        @Nullable String extract = data.get(key);
        return Optional.ofNullable(extract);
    }

    @Override
    public Optional<String> getString(@NotNull Supplier<String> key) {
        return getString(key.get());
    }

    @Override
    public @Nullable String getStringOrNull(String key) {
        @Nullable String extract = data.get(key);
        return key;
    }

    @Override
    public @Nullable String getStringOrNull(@NotNull Supplier<String> key) {
        return getStringOrNull(key.get());
    }

    @Override
    public Optional<Integer> getInteger(String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.valueOf(key));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Integer> getInteger(@NotNull Supplier<String> key) {
        return getInteger(key.get());
    }

    @Override
    public @Nullable Integer getIntegerOrNull(String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return null;
        }
        try {
            return Integer.valueOf(key);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public @Nullable Integer getIntegerOrNull(@NotNull Supplier<String> key) {
        return getIntegerOrNull(key.get());
    }

    @Override
    public Optional<Double> getDouble(String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Double.valueOf(key));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Double> getDouble(@NotNull Supplier<String> key) {
        return getDouble(key.get());
    }

    @Override
    public @Nullable Double getDoubleOrNull(String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return null;
        }
        try {
            return Double.parseDouble(key);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public @Nullable Double getDoubleOrNull(@NotNull Supplier<String> key) {
        return getDoubleOrNull(key.get());
    }

    @Override
    public Optional<Long> getLong(String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Long.parseLong(key));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Long> getLong(@NotNull Supplier<String> key) {
        return getLong(key.get());
    }

    @Override
    public @Nullable Long getLongOrNull(String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return null;
        }
        try {
            return Long.parseLong(key);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public @Nullable Long getLongOrNull(@NotNull Supplier<String> key) {
        return getLongOrNull(key.get());
    }

    @Override
    public Optional<Short> getShort(String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Short.parseShort(key));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Short> getShort(@NotNull Supplier<String> key) {
        return getShort(key.get());
    }

    @Override
    public @Nullable Short getShortOrNull(String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return null;
        }
        try {
            return Short.parseShort(key);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public @Nullable Short getShortOrNull(@NotNull Supplier<String> key) {
        return getShortOrNull(key.get());
    }

    @Override
    public Optional<Byte> getByte(String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Byte.parseByte(key));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Byte> getByte(@NotNull Supplier<String> key) {
        return getByte(key.get());
    }

    @Override
    public @Nullable Byte getByteOrNull(String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return null;
        }
        try {
            return Byte.parseByte(key);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public @Nullable Byte getByteOrNull(@NotNull Supplier<String> key) {
        return getByteOrNull(key.get());
    }

    @Override
    public Optional<Boolean> getBoolean(String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Boolean.parseBoolean(key));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Boolean> getBoolean(@NotNull Supplier<String> key) {
        return getBoolean(key.get());
    }

    @Override
    public @Nullable Boolean getBooleanOrNull(String key) {
        @Nullable String extract = data.get(key);
        if (extract == null) {
            return null;
        }
        try {
            return Boolean.parseBoolean(key);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public @Nullable Boolean getBooleanOrNull(@NotNull Supplier<String> key) {
        return getBooleanOrNull(key.get());
    }

    //legacy interpreter
    @Override
    public @MaybePresent <T> Optional<T> get(String key, Function<String, T> interpreter) {
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
    public @MaybePresent <T> Optional<T> get(@NotNull Supplier<String> key, Function<String, T> interpreter) {
        return get(key.get(), interpreter);
    }

    //legacy interpreter
    @Override
    public <T> @Nullable T getOrNull(String key, Function<String, T> interpreter) {
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
    public <T> @Nullable T getOrNull(@NotNull Supplier<String> key, Function<String, T> interpreter) {
        return getOrNull(key.get(), interpreter);
    }

    @Override
    public @MaybePresent <T> Optional<T> get(String key, Interpreter<T> interpreter) {
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
    public @MaybePresent <T> Optional<T> get(@NotNull Supplier<String> key, Interpreter<T> interpreter) {
        return get(key.get(), interpreter);
    }

    @Override
    public <T> @Nullable T getOrNull(String key, Interpreter<T> interpreter) {
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
    public <T> @Nullable T getOrNull(@NotNull Supplier<String> key, Interpreter<T> interpreter) {
        return getOrNull(key.get(), interpreter);
    }

    @Override
    public void setString(String key, @NotNull String value) {
        data.put(key, value.toString());
    }

    @Override
    public void setString(String key, @NotNull Supplier<String> value) {
        setString(key, value.get());
    }

    @Override
    public void setInteger(String key, @NotNull Integer value) {
        data.put(key, value.toString());
    }

    @Override
    public void setInteger(String key, @NotNull Supplier<Integer> value) {
        setInteger(key, value.get());
    }

    @Override
    public void setDouble(String key, @NotNull Double value) {
        data.put(key, value.toString());
    }

    @Override
    public void setDouble(String key, @NotNull Supplier<Double> value) {
        setDouble(key, value.get());
    }

    @Override
    public void setLong(String key, @NotNull Long value) {
        data.put(key, value.toString());
    }

    @Override
    public void setLong(String key, @NotNull Supplier<Long> value) {
        setLong(key, value.get());
    }

    @Override
    public void setShort(String key, @NotNull Short value) {
        data.put(key, value.toString());
    }

    @Override
    public void setShort(String key, @NotNull Supplier<Short> value) {
        setShort(key, value.get());
    }

    @Override
    public void setByte(String key, @NotNull Byte value) {
        data.put(key, value.toString());
    }

    @Override
    public void setByte(String key, @NotNull Supplier<Byte> value) {
        setByte(key, value.get());
    }

    @Override
    public void setBoolean(String key, @NotNull Boolean value) {
        data.put(key, value.toString());
    }

    @Override
    public void setBoolean(String key, @NotNull Supplier<Boolean> value) {
        setBoolean(key, value.get());
    }

    @Override
    public <T> void set(String key, T value, @NotNull Interpreter<T> interpreter) {
        data.put(key, interpreter.reverse(value));
    }

    @Override
    public <T> void set(String key, @NotNull Supplier<T> value, Interpreter<T> interpreter) {
        set(key, value.get(), interpreter);
    }

    @Override
    public <T> void set(@NotNull Supplier<String> key, T value, Interpreter<T> interpreter) {
        set(key.get(), value, interpreter);
    }

    @Override
    public <T> void set(@NotNull Supplier<String> key, @NotNull Supplier<T> value, Interpreter<T> interpreter) {
        set(key.get(), value.get(), interpreter);
    }

    @Override
    public void del(String key) {
        data.remove(key);
    }

    @Override
    public void del(@NotNull Supplier<String> key) {
        del(key.get());
    }

    @Override
    public void del(String... key) {
        dellAllFromCollection(List.of(key));
    }

    @Override
    public void delAll(@NotNull Supplier<Collection<String>> keys) {
        Collection<String> collection = keys.get();
        dellAllFromCollection(collection);
    }

    public void dellAllFromCollection(Collection<String> keys) {
        data.removeAll(mapEntry -> keys.contains(mapEntry.getKey()));
    }


    //query's


    @Override
    public <T> T query(@NotNull Function<Supplier<String>, T> query, String input) {
        updateHazel();
        return query.apply(() -> input);
    }

    @Override
    public <T> T query(@NotNull BiFunction<Supplier<String>, Interpreter<T>, T> query, String input, Interpreter<T> interpreter) {
        updateHazel();
        return query.apply(() -> input, interpreter);
    }

    @Override
    public <T> void query(@NotNull BiConsumer<String, Supplier<T>> query, String input, Supplier<T> t) {
        updateHazel();
        query.accept(input, t);
    }

    @Override
    public <T> void query(@NotNull TriConsumer<String, T, Supplier<Interpreter<T>>> query, String input, Interpreter<T> interpreter, @NotNull Supplier<T> t) {
        updateHazel();
        query.consume(input, t.get(), () -> interpreter);
    }

    public String header_comment() {
        return header_comment;
    }

    public void setHeader_comment(String header_comment) {
        this.header_comment = header_comment;
    }

    public String getName() {
        return name;
    }

    @Override
    public int size() {
        return data.size();
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

    @Override
    public String toString() {
        return name;
    }
}
