package net.juligames.core.api.config;

import net.juligames.core.api.misc.TriConsumer;
import org.checkerframework.checker.optional.qual.MaybePresent;
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
 * @see BuildInInterpreters
 */
public interface Configuration {

    /**
     * This keys can be requested but never should be overridden (it is possible to override them, but only consider doing this if you really
     * know what you are doing)
     */
    String[] RESERVED_KEYS = new String[]{"configuration_header", "configuration_name"};

    void updateHazel();

     Map<String,String> export();

     Set<String> keySet();

     Set<Map.Entry<String,String>> entrySet();

     Collection<String> values();

     void writeTo(OutputStream dataStream) throws IOException;

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


    void setString(String key, String value);
    void setString(String key, Supplier<String> value);

    void setInteger(String key, Integer value);
    void setInteger(String key, Supplier<Integer> value);

    void setDouble(String key, Double value);
    void setDouble(String key, Supplier<Double> value);

    void setLong(String key, Long value);
    void setLong(String key, Supplier<Long> value);

    void setShort(String key, Short value);
    void setShort(String key, Supplier<Short> value);

    void setByte(String key, Byte value);
    void setByte(String key, Supplier<Byte> value);

    void setBoolean(String key, Boolean value);
    void setBoolean(String key, Supplier<Boolean> value);

    <T> void set(String key, T value, Interpreter<T> interpreter);
    <T> void set(String key, Supplier<T> value, Interpreter<T> interpreter);

    <T> void set(Supplier<String> key, T value, Interpreter<T> interpreter);
    <T> void set(Supplier<String> key, Supplier<T> value, Interpreter<T> interpreter);

    void del(String key);
    void del(Supplier<String> keys);

    void del(String... key);
    void delAll(Supplier<Collection<String>> keys);

    <T> T query(Function<Supplier<String>, T> query, String input);
    <T> T query(BiFunction<Supplier<String>, Interpreter<T> , T> query, String input, Interpreter<T> interpreter);

    <T> void query(BiConsumer<String,Supplier<T>> query, String input, Supplier<T> t);

    <T> void query(TriConsumer<String,T,Supplier<Interpreter<T>>> query, String input, Interpreter<T> interpreter, Supplier<T> t);


 }
