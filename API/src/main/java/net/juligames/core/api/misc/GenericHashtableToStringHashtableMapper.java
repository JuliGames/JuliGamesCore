package net.juligames.core.api.misc;

import net.juligames.core.api.config.Interpreter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Hashtable;
import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 03.03.2023
 */
@ApiStatus.AvailableSince("1.5")
public class GenericHashtableToStringHashtableMapper<K, V> implements Function<Hashtable<K, V>, Hashtable<String, String>> {

    private final @NotNull Interpreter<K> kInterpreter;
    private final @NotNull Interpreter<V> vInterpreter;

    public GenericHashtableToStringHashtableMapper(@NotNull Interpreter<K> kInterpreter, @NotNull Interpreter<V> vInterpreter) {
        this.kInterpreter = kInterpreter;
        this.vInterpreter = vInterpreter;

    }

    @Contract(value = "_ -> new", pure = true)
    public static <T> @NotNull GenericHashtableToStringHashtableMapper<T, T> createParallel(final Interpreter<T> tInterpreter) {
        return new GenericHashtableToStringHashtableMapper<>(tInterpreter, tInterpreter);
    }

    @Override
    public @NotNull Hashtable<String, String> apply(@NotNull Hashtable<K, V> objectObjectHashtable) {
        final Hashtable<String, String> stringStringHashtable = new Hashtable<>();
        objectObjectHashtable.forEach((key, value) ->
                stringStringHashtable.put(kInterpreter.reverse(key), vInterpreter.reverse(value)));
        return stringStringHashtable;
    }
}
