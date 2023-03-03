package net.juligames.core.api.config;

import de.bentzin.tools.pair.BasicPair;
import de.bentzin.tools.pair.EntryPairAdapter;
import de.bentzin.tools.pair.Pair;
import net.juligames.core.api.misc.TriConsumer;
import org.checkerframework.checker.optional.qual.MaybePresent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.function.*;

/**
 * @author Ture Bentzin
 * 26.11.2022
 * @apiNote all "orNull" methods will not throw a {@link RuntimeException} on failed parsing, but the normal methods
 * will do so
 * @see BuildInInterpreters
 */
@SuppressWarnings("unused")
public interface Configuration extends Comparable<Configuration>, Iterable<Pair<String>> {


    /**
     * This keys can be requested but never should be overridden (it is possible to override them, but only consider doing this if you really
     * know what you are doing)
     */
    String[] RESERVED_KEYS = new String[]{"configuration_header", "configuration_name"};

    void updateHazel();

    @NotNull Map<String, String> export();

    @NotNull Set<String> keySet();

    @NotNull Set<Map.Entry<String, String>> entrySet();

    @NotNull Collection<String> values();

    void writeTo(@NotNull OutputStream dataStream) throws IOException;

    @NotNull Properties cloneToProperties();

    /**
     * Checks if the key is set
     */
    @ApiStatus.AvailableSince("1.4")
    boolean isSet(@NotNull String key);

    /**
     * Checks if the key is set
     */
    @ApiStatus.AvailableSince("1.4")
    boolean isSet(@NotNull Supplier<String> key);

    /**
     * Checks if the key is set and can be parsed without exception by the given interpreter
     *
     * @apiNote This should only be used if "getting" of the data is not relevant, and validation is all you need!
     */
    @ApiStatus.AvailableSince("1.4")
    <T> boolean isSetAndValid(@NotNull String key, @NotNull Interpreter<T> interpreter);

    /**
     * Checks if the key is set and can be parsed without exception by the given interpreter
     *
     * @apiNote This should only be used if "getting" of the data is not relevant, and validation is all you need!
     */
    @ApiStatus.AvailableSince("1.4")
    <T> boolean isSetAndValid(@NotNull Supplier<String> key, @NotNull Interpreter<T> interpreter);

    @NotNull Optional<String> getString(@NotNull String key);

    @NotNull Optional<String> getString(@NotNull Supplier<String> key);

    @Nullable String getStringOrNull(@NotNull String key);

    @Nullable String getStringOrNull(@NotNull Supplier<String> key);

    @NotNull Optional<Integer> getInteger(@NotNull String key);

    @NotNull Optional<Integer> getInteger(@NotNull Supplier<String> key);

    @Nullable Integer getIntegerOrNull(@NotNull String key);

    @Nullable Integer getIntegerOrNull(@NotNull Supplier<String> key);

    @NotNull Optional<Double> getDouble(@NotNull String key);

    @NotNull Optional<Double> getDouble(@NotNull Supplier<String> key);

    @Nullable Double getDoubleOrNull(@NotNull String key);

    @Nullable Double getDoubleOrNull(@NotNull Supplier<String> key);

    @NotNull Optional<Long> getLong(@NotNull String key);

    @NotNull Optional<Long> getLong(@NotNull Supplier<String> key);

    @Nullable Long getLongOrNull(@NotNull String key);

    @Nullable Long getLongOrNull(@NotNull Supplier<String> key);

    @NotNull Optional<Short> getShort(@NotNull String key);

    @NotNull Optional<Short> getShort(@NotNull Supplier<String> key);

    @Nullable Short getShortOrNull(@NotNull String key);

    @Nullable Short getShortOrNull(@NotNull Supplier<String> key);

    @NotNull Optional<Byte> getByte(@NotNull String key);

    @NotNull Optional<Byte> getByte(@NotNull Supplier<String> key);

    @Nullable Byte getByteOrNull(@NotNull String key);

    @Nullable Byte getByteOrNull(@NotNull Supplier<String> key);

    @NotNull Optional<Boolean> getBoolean(@NotNull String key);

    @NotNull Optional<Boolean> getBoolean(@NotNull Supplier<String> key);

    @Nullable Boolean getBooleanOrNull(@NotNull String key);

    @Nullable Boolean getBooleanOrNull(@NotNull Supplier<String> key);

    @NotNull Optional<Float> getFloat(@NotNull String key);

    @NotNull Optional<Float> getFloat(@NotNull Supplier<String> key);

    @Nullable Float getFloatOrNull(@NotNull String key);

    @Nullable Float getFloatOrNull(@NotNull Supplier<String> key);

    @NotNull <T> Collection<T> getCollection(@NotNull String keyspace, @NotNull Interpreter<T> interpreter);

    @NotNull <T> Collection<T> getCollection(@NotNull Supplier<String> keyspace, @NotNull Interpreter<T> interpreter);

    @Deprecated
    @MaybePresent <T> Optional<T> get(@NotNull String key, @NotNull Function<String, T> interpreter);

    @Deprecated
    @MaybePresent <T> Optional<T> get(@NotNull Supplier<String> key, @NotNull Function<String, T> interpreter);

    @Deprecated
    @Nullable <T> T getOrNull(@NotNull String key, @NotNull Function<String, T> interpreter);

    @Deprecated
    @Nullable <T> T getOrNull(@NotNull Supplier<String> key, @NotNull Function<String, T> interpreter);

    @MaybePresent <T> Optional<T> get(@NotNull String key, @NotNull Interpreter<T> interpreter);

    @MaybePresent <T> Optional<T> get(@NotNull Supplier<String> key, @NotNull Interpreter<T> interpreter);

    @Nullable <T> T getOrNull(@NotNull String key, @NotNull Interpreter<T> interpreter);

    @Nullable <T> T getOrNull(@NotNull Supplier<String> key, @NotNull Interpreter<T> interpreter);


    void setString(@NotNull String key, @NotNull String value);

    void setString(@NotNull String key, @NotNull Supplier<String> value);

    void setInteger(@NotNull String key, @NotNull Integer value);

    void setInteger(@NotNull String key, @NotNull Supplier<Integer> value);

    void setDouble(@NotNull String key, @NotNull Double value);

    void setDouble(@NotNull String key, @NotNull Supplier<Double> value);

    void setLong(@NotNull String key, @NotNull Long value);

    void setLong(@NotNull String key, @NotNull Supplier<Long> value);

    void setShort(@NotNull String key, @NotNull Short value);

    void setShort(@NotNull String key, @NotNull Supplier<Short> value);

    void setByte(@NotNull String key, @NotNull Byte value);

    void setByte(@NotNull String key, @NotNull Supplier<Byte> value);

    void setBoolean(@NotNull String key, @NotNull Boolean value);

    void setBoolean(@NotNull String key, @NotNull Supplier<Boolean> value);

    void setFloat(@NotNull String key, @NotNull Float value);

    void setFloat(@NotNull String key, @NotNull Supplier<Float> value);

    <T> void set(@NotNull String key, @NotNull T value, @NotNull Interpreter<T> interpreter);

    <T> void set(@NotNull String key, @NotNull Supplier<T> value, @NotNull Interpreter<T> interpreter);

    <T> void set(@NotNull Supplier<String> key, @NotNull T value, @NotNull Interpreter<T> interpreter);

    <T> void set(@NotNull Supplier<String> key, @NotNull Supplier<T> value, @NotNull Interpreter<T> interpreter);

    <T> void setIterable(@NotNull String keySpace, @NotNull Iterable<T> iterable, @NotNull Interpreter<T> interpreter);

    <T> void setIterable(@NotNull Supplier<String> keySpace, @NotNull Iterable<T> iterable, @NotNull Interpreter<T> interpreter);

    <T> void setIterable(@NotNull String keySpace, @NotNull Supplier<Iterable<T>> iterable, @NotNull Interpreter<T> interpreter);

    <T> void setIterable(@NotNull Supplier<String> keySpace, @NotNull Supplier<Iterable<T>> iterable, @NotNull Interpreter<T> interpreter);

    void set(@NotNull BasicPair<String, String> basicPair);

    //ifAbsent

    @ApiStatus.AvailableSince("1.5")
    void setStringIfAbsent(@NotNull String key, @NotNull String value);

    @ApiStatus.AvailableSince("1.5")
    void setStringIfAbsent(@NotNull String key, @NotNull Supplier<String> value);

    @ApiStatus.AvailableSince("1.5")
    void setIntegerIfAbsent(@NotNull String key, @NotNull Integer value);

    @ApiStatus.AvailableSince("1.5")
    void setIntegerIfAbsent(@NotNull String key, @NotNull Supplier<Integer> value);

    @ApiStatus.AvailableSince("1.5")
    void setDoubleIfAbsent(@NotNull String key, @NotNull Double value);

    @ApiStatus.AvailableSince("1.5")
    void setDoubleIfAbsent(@NotNull String key, @NotNull Supplier<Double> value);

    @ApiStatus.AvailableSince("1.5")
    void setLongIfAbsent(@NotNull String key, @NotNull Long value);

    @ApiStatus.AvailableSince("1.5")
    void setLongIfAbsent(@NotNull String key, @NotNull Supplier<Long> value);

    @ApiStatus.AvailableSince("1.5")
    void setShortIfAbsent(@NotNull String key, @NotNull Short value);

    @ApiStatus.AvailableSince("1.5")
    void setShortIfAbsent(@NotNull String key, @NotNull Supplier<Short> value);

    @ApiStatus.AvailableSince("1.5")
    void setByteIfAbsent(@NotNull String key, @NotNull Byte value);

    @ApiStatus.AvailableSince("1.5")
    void setByteIfAbsent(@NotNull String key, @NotNull Supplier<Byte> value);

    @ApiStatus.AvailableSince("1.5")
    void setBooleanIfAbsent(@NotNull String key, @NotNull Boolean value);

    @ApiStatus.AvailableSince("1.5")
    void setBooleanIfAbsent(@NotNull String key, @NotNull Supplier<Boolean> value);

    @ApiStatus.AvailableSince("1.5")
    void setFloatIfAbsent(@NotNull String key, @NotNull Float value);

    @ApiStatus.AvailableSince("1.5")
    void setFloatIfAbsent(@NotNull String key, @NotNull Supplier<Float> value);

    @ApiStatus.AvailableSince("1.5")
    <T> void setIfAbsent(@NotNull String key, @NotNull T value, @NotNull Interpreter<T> interpreter);

    @ApiStatus.AvailableSince("1.5")
    <T> void setIfAbsent(@NotNull String key, @NotNull Supplier<T> value, @NotNull Interpreter<T> interpreter);

    @ApiStatus.AvailableSince("1.5")
    <T> void setIfAbsent(@NotNull Supplier<String> key, @NotNull T value, @NotNull Interpreter<T> interpreter);

    @ApiStatus.AvailableSince("1.5")
    <T> void setIfAbsent(@NotNull Supplier<String> key, @NotNull Supplier<T> value, @NotNull Interpreter<T> interpreter);

    @ApiStatus.AvailableSince("1.5")
    void setIfAbsent(@NotNull BasicPair<String, String> basicPair);

    //del

    void del(@NotNull String key);

    void del(@NotNull Supplier<String> keys);

    void del(@NotNull String @NotNull ... key);

    void delAll(@NotNull Supplier<Collection<String>> keys);

    void delRecursive(@NotNull String key);

    void delRecursive(@NotNull Supplier<String> key);

    void delRecursive(@NotNull String @NotNull ... keys);

    void delAllRecursive(@NotNull Supplier<Collection<String>> keys);

    <T> T query(@NotNull Function<Supplier<String>, @NotNull T> query, @NotNull String input);

    <T> T query(@NotNull BiFunction<Supplier<String>, @NotNull Interpreter<T>, T> query, @NotNull String input, @NotNull Interpreter<T> interpreter);

    <T> void query(@NotNull BiConsumer<String, Supplier<T>> query, @NotNull String input, @NotNull Supplier<T> t);

    <T> void query(@NotNull TriConsumer<String, @NotNull T, @NotNull Supplier<Interpreter<T>>> query, @NotNull String input, @NotNull Interpreter<T> interpreter, @NotNull Supplier<T> t);

    void feed(@NotNull String key, @NotNull Consumer<String> rawConsumer);

    int size();

    void copyAndAppendContentTo(@NotNull Configuration configuration);

    void copyAndOverrideContentTo(@NotNull Configuration configuration);

    @NotNull Configuration copy(@NotNull String name);

    @NotNull Configuration copyToOffline(@NotNull String name);

    @NotNull Configuration copyToOffline();

    /**
     * This Method will add every entry out of default if it is not set here!
     *
     * @param defaults the defaults
     */
    @ApiStatus.AvailableSince("1.4")
    void applyDefaults(@NotNull Properties defaults);

    void appendAll(@NotNull Collection<Configuration> configurations);

    @ApiStatus.Experimental
    @ApiStatus.AvailableSince("1.4")
    <R> R doWithData(@NotNull Function<Map<String, String>, R> function);

    @ApiStatus.Experimental
    @ApiStatus.AvailableSince("1.4")
    void doWithData(@NotNull Consumer<Map<String, String>> action);

    @Override
    default int compareTo(@NotNull Configuration o) {
        return o.size() - size();
    }


    //from map

    /**
     * Performs the given action for each entry in this map until all entries
     * have been processed or the action throws an exception.   Unless
     * otherwise specified by the implementing class, actions are performed in
     * the order of entry set iteration (if an iteration order is specified.)
     * Exceptions thrown by the action are relayed to the caller.
     *
     * @param action The action to be performed for each entry
     * @throws NullPointerException            if the specified action is null
     * @throws ConcurrentModificationException if an entry is found to be
     *                                         removed during iteration
     * @implSpec The default implementation is equivalent to, for this {@code map}:
     * <pre> {@code
     * for (Map.Entry<String, String> entry : map.entrySet())
     *     action.accept(entry.getKey(), entry.getValue());
     * }</pre>
     * <p>
     * The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     */
    @ApiStatus.AvailableSince("1.5")
    default void forEach(BiConsumer<String, String> action) {
        Objects.requireNonNull(action);
        for (Map.Entry<String, String> entry : entrySet()) {
            String k;
            String v;
            try {
                k = entry.getKey();
                v = entry.getValue();
            } catch (IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }
            action.accept(k, v);
        }
    }


    //Iterable

    @NotNull
    @Override
    @ApiStatus.AvailableSince("1.5")
    default Iterator<Pair<String>> iterator() {
        return entrySet().stream().map(EntryPairAdapter::pairFromEntry).iterator();
    }

    @Override
    @ApiStatus.AvailableSince("1.5")
    default void forEach(Consumer<? super Pair<String>> action) {
        forEach((s, s2) -> action.accept(new Pair<>(s, s2)));
    }

    @Override
    @ApiStatus.AvailableSince("1.5")
    default Spliterator<Pair<String>> spliterator() {
        return entrySet().stream().map(EntryPairAdapter::pairFromEntry).spliterator();
    }


}
