package net.juligames.core.api.config;

import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Ture Bentzin
 * 26.11.2022
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

    Optional<Boolean> getBoolean(String key);
    Optional<Boolean> getBoolean(Supplier<String> key);
    @Nullable Boolean getBooleanOrNull(String key);
    @Nullable Boolean getBooleanOrNull(Supplier<String> key);

 }
