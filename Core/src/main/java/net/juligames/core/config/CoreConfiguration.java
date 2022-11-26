package net.juligames.core.config;

import com.hazelcast.map.IMap;
import net.juligames.core.Core;
import net.juligames.core.api.TODO;
import net.juligames.core.api.config.Configuration;
import net.juligames.core.api.config.Interpreter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import org.checkerframework.checker.optional.qual.MaybePresent;

/**
 * @author Ture Bentzin
 * 26.11.2022
 */
@TODO(doNotcall = true)
public class CoreConfiguration implements Configuration {

    private final String name;
    private IMap<String,String> data; //May be removed

    public CoreConfiguration(String name) {
        this.name = name;
        data = hazel();
    }


    private @NotNull IMap<String,String> hazel() {
        return Core.getInstance().getOrThrow().getMap(name);
    }

    public void updateHazel() {
        data = hazel();
    }

    public Map<String,String> export()  {
        return new HashMap<>(data);
    }

    public Set<String> keySet() {
        return hazel().keySet();
    }

    public Set<Map.Entry<String,String>> entrySet() {
        return hazel().entrySet();
    }

    public Collection<String> values() {
        return hazel().values();
    }

    @Override
    public Properties cloneToProperties() {
        return null;
    }

    @Override
    public Optional<String> getString(String key) {
        return Optional.empty();
    }

    @Override
    public Optional<String> getString(Supplier<String> key) {
        return Optional.empty();
    }

    @Override
    public @Nullable String getStringOrNull(String key) {
        return null;
    }

    @Override
    public @Nullable String getStringOrNull(Supplier<String> key) {
        return null;
    }

    @Override
    public Optional<Integer> getInteger(String key) {
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getInteger(Supplier<String> key) {
        return Optional.empty();
    }

    @Override
    public @Nullable Integer getIntegerOrNull(String key) {
        return null;
    }

    @Override
    public @Nullable Integer getIntegerOrNull(Supplier<String> key) {
        return null;
    }

    @Override
    public Optional<Double> getDouble(String key) {
        return Optional.empty();
    }

    @Override
    public Optional<Double> getDouble(Supplier<String> key) {
        return Optional.empty();
    }

    @Override
    public @Nullable Double getDoubleOrNull(String key) {
        return null;
    }

    @Override
    public @Nullable Double getDoubleOrNull(Supplier<String> key) {
        return null;
    }

    @Override
    public Optional<Long> getLong(String key) {
        return Optional.empty();
    }

    @Override
    public Optional<Long> getLong(Supplier<String> key) {
        return Optional.empty();
    }

    @Override
    public @Nullable Long getLongOrNull(String key) {
        return null;
    }

    @Override
    public @Nullable Long getLongOrNull(Supplier<String> key) {
        return null;
    }

    @Override
    public Optional<Short> getShort(String key) {
        return Optional.empty();
    }

    @Override
    public Optional<Short> getShort(Supplier<String> key) {
        return Optional.empty();
    }

    @Override
    public @Nullable Short getShortOrNull(String key) {
        return null;
    }

    @Override
    public @Nullable Short getShortOrNull(Supplier<String> key) {
        return null;
    }

    @Override
    public Optional<Byte> getByte(String key) {
        return Optional.empty();
    }

    @Override
    public Optional<Byte> getByte(Supplier<String> key) {
        return Optional.empty();
    }

    @Override
    public @Nullable Byte getByteOrNull(String key) {
        return null;
    }

    @Override
    public @Nullable Byte getByteOrNull(Supplier<String> key) {
        return null;
    }

    @Override
    public Optional<Boolean> getBoolean(String key) {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> getBoolean(Supplier<String> key) {
        return Optional.empty();
    }

    @Override
    public @Nullable Boolean getBooleanOrNull(String key) {
        return null;
    }

    @Override
    public @Nullable Boolean getBooleanOrNull(Supplier<String> key) {
        return null;
    }

    @Override
    public @MaybePresent <T> Optional<T> get(String key, Function<String, T> interpreter) {
        return Optional.empty();
    }

    @Override
    public @MaybePresent <T> Optional<T> get(Supplier<String> key, Function<String, T> interpreter) {
        return Optional.empty();
    }

    @Override
    public <T> @Nullable T getOrNull(String key, Function<String, T> interpreter) {
        return null;
    }

    @Override
    public <T> @Nullable T getOrNull(Supplier<String> key, Function<String, T> interpreter) {
        return null;
    }

    @Override
    public @MaybePresent <T> Optional<T> get(String key, Interpreter<T> interpreter) {
        return Optional.empty();
    }

    @Override
    public @MaybePresent <T> Optional<T> get(Supplier<String> key, Interpreter<T> interpreter) {
        return Optional.empty();
    }

    @Override
    public <T> @Nullable T getOrNull(String key, Interpreter<T> interpreter) {
        return null;
    }

    @Override
    public <T> @Nullable T getOrNull(Supplier<String> key, Interpreter<T> interpreter) {
        return null;
    }
}
