package net.juligames.core.api.misc;

import net.juligames.core.api.config.Interpreter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@ApiStatus.AvailableSince("1.6")
public class EntryInterpretationUtil {

    private EntryInterpretationUtil() {

    }

    /**
     * Interpret an entry in a {@link Map} using the given key and value interpreters and return an unmodifiable map entry.
     *
     * @param stringStringEntry the map entry to interpret
     * @param kInterpreter      the key interpreter
     * @param vInterpreter      the value interpreter
     * @param <K>               the type of the key
     * @param <V>               the type of the value
     * @return an unmodifiable map entry with an interpreted key and value
     * @throws Exception if there is an error during interpretation
     */
    @Contract("_, _, _ -> new")
    public static <K, V> Map.@NotNull @Unmodifiable Entry<K, V> interpretEntry(Map.@NotNull Entry<String, String> stringStringEntry, @NotNull Interpreter<K> kInterpreter, @NotNull Interpreter<V> vInterpreter) throws Exception {
        return Map.entry(kInterpreter.interpret(stringStringEntry.getKey()), vInterpreter.interpret(stringStringEntry.getValue()));
    }

    /**
     * Reverse an entry in a {@link Map} using the given key and value interpreters and return an unmodifiable map entry.
     *
     * @param stringStringEntry the map entry to reverse
     * @param kInterpreter      the key interpreter
     * @param vInterpreter      the value interpreter
     * @param <K>               the type of the key
     * @param <V>               the type of the value
     * @return an unmodifiable map entry with reversed key and value
     */
    @Contract("_, _, _ -> new")
    public static <K, V> Map.@NotNull @Unmodifiable Entry<String, String> reverseEntry(Map.@NotNull Entry<K, V> stringStringEntry, @NotNull Interpreter<K> kInterpreter, @NotNull Interpreter<V> vInterpreter) {
        return Map.entry(kInterpreter.reverse(stringStringEntry.getKey()), vInterpreter.reverse(stringStringEntry.getValue()));
    }

    /**
     * Reverse a collection of entries in a {@link Map} using the given key and value interpreters and return an unmodifiable collection of map entries.
     *
     * @param collection   the collection of map entries to reverse
     * @param kInterpreter the key interpreter
     * @param vInterpreter the value interpreter
     * @param <K>          the type of the key
     * @param <V>          the type of the value
     * @return an unmodifiable collection of map entries with reversed key and value
     */
    public static <K, V> @Unmodifiable Collection<Map.Entry<String, String>> reverseEntries(@NotNull Collection<Map.Entry<K, V>> collection, Interpreter<K> kInterpreter, Interpreter<V> vInterpreter) {
        return collection.stream().map(kvEntry -> reverseEntry(kvEntry, kInterpreter, vInterpreter)).collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Interpret a collection of entries in a {@link Map} using the given key and value interpreters and return an unmodifiable collection of map entries.
     *
     * @param collection   the collection of map entries to interpret
     * @param kInterpreter the key interpreter
     * @param vInterpreter the value interpreter
     * @param <K>          the type of the key
     * @param <V>          the type of the value
     * @return an unmodifiable collection of map entries with interpreted key and value
     * @throws RuntimeException if there is an error during interpretation
     */
    public static <K, V> @Unmodifiable Collection<Map.Entry<K, V>> interpretEntries(@NotNull Collection<Map.Entry<String, String>> collection, Interpreter<K> kInterpreter, Interpreter<V> vInterpreter) {
        return collection.stream().map(kvEntry -> {
            try {
                return interpretEntry(kvEntry, kInterpreter, vInterpreter);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toUnmodifiableSet());
    }

}
