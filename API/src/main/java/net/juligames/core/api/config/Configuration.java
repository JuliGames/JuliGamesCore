package net.juligames.core.api.config;

import org.checkerframework.checker.optional.qual.MaybePresent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Ture Bentzin
 * 26.11.2022
 * @see BuildInInterpreters
 */
public interface Configuration {

    Properties cloneToProperties();

    Optional<String> getString(String key);
    Optional<String> getString(Supplier<String> key);
    @Nullable String getStringOrNull(String key);
    @Nullable String getStringOrNull(Supplier<String> key);

    Optional<Integer> getInteger(String key);
    Optional<Integer> getInteger(Supplier<String> key);
    @Nullable Integer getIntegerOrNull(String key);
    @Nullable Integer getIntegerOrNull(Supplier<String> key);

    Optional<Double> getDouble(String key);
    Optional<Double> getDouble(Supplier<String> key);
    @Nullable Double getDoubleOrNull(String key);
    @Nullable Double getDoubleOrNull(Supplier<String> key);

    Optional<Long> getLong(String key);
    Optional<Long> getLong(Supplier<String> key);
    @Nullable Long getLongOrNull(String key);
    @Nullable Long getLongOrNull(Supplier<String> key);

    Optional<Short> getShort(String key);
    Optional<Short> getShort(Supplier<String> key);
    @Nullable Short getShortOrNull(String key);
    @Nullable Short getShortOrNull(Supplier<String> key);

    Optional<Byte> getByte(String key);
    Optional<Byte> getByte(Supplier<String> key);
    @Nullable Byte getByteOrNull(String key);
    @Nullable Byte getByteOrNull(Supplier<String> key);

    Optional<Boolean> getBoolean(String key);
    Optional<Boolean> getBoolean(Supplier<String> key);
    @Nullable Boolean getBooleanOrNull(String key);
    @Nullable Boolean getBooleanOrNull(Supplier<String> key);

    @Deprecated
    @MaybePresent
    <T> Optional<T>get(String key, Function<String,T> interpreter);
    @Deprecated
    @MaybePresent
    <T>Optional<T> get(Supplier<String> key, Function<String,T> interpreter);
    @Deprecated
    @Nullable <T>T getOrNull(String key,  Function<String,T> interpreter);
    @Deprecated
    @Nullable <T>T getOrNull(Supplier<String> key,  Function<String,T> interpreter);

    @MaybePresent
    <T> Optional<T>get(String key, Interpreter<T> interpreter);
    @MaybePresent
    <T>Optional<T> get(Supplier<String> key, Interpreter<T> interpreter);
    @Nullable <T>T getOrNull(String key,  Interpreter<T> interpreter);
    @Nullable <T>T getOrNull(Supplier<String> key,  Interpreter<T> interpreter);

 }
