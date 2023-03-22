package net.juligames.core.api.misc;

import net.juligames.core.api.config.Interpreter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class EntryInterpretationUtil {

    private EntryInterpretationUtil() {

    }

    @Contract("_, _, _ -> new")
    public static <K, V> Map.@NotNull @Unmodifiable Entry<K, V> interpretEntry(Map.@NotNull Entry<String, String> stringStringEntry, @NotNull Interpreter<K> kInterpreter, @NotNull Interpreter<V> vInterpreter) throws Exception {
        return Map.entry(kInterpreter.interpret(stringStringEntry.getKey()), vInterpreter.interpret(stringStringEntry.getValue()));
    }

    @Contract("_, _, _ -> new")
    public static <K, V> Map.@NotNull @Unmodifiable Entry<String, String> reverseEntry(Map.@NotNull Entry<K, V> stringStringEntry, @NotNull Interpreter<K> kInterpreter, @NotNull Interpreter<V> vInterpreter) {
        return Map.entry(kInterpreter.reverse(stringStringEntry.getKey()), vInterpreter.reverse(stringStringEntry.getValue()));
    }

    public static <K, V> @Unmodifiable Collection<Map.Entry<String, String>> reverseEntries(@NotNull Collection<Map.Entry<K, V>> collection, Interpreter<K> kInterpreter, Interpreter<V> vInterpreter) {
        return collection.stream().map(kvEntry -> reverseEntry(kvEntry, kInterpreter, vInterpreter)).collect(Collectors.toUnmodifiableSet());
    }


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
