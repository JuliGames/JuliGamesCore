package net.juligames.core.api.misc;

import net.juligames.core.api.config.Interpreter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Hashtable;
import java.util.function.Function;

/**
 * The GenericHashtableToStringHashtableMapper class is a function that maps a Hashtable with generic key-value types
 * to a Hashtable with String keys and String values.
 * It uses provided interpreters to convert the keys and values of the input Hashtable to their String representations.
 *
 * @author Ture Bentzin
 * 03.03.2023
 */
@ApiStatus.AvailableSince("1.5")
public class GenericHashtableToStringHashtableMapper<K, V> implements Function<Hashtable<K, V>, Hashtable<String, String>> {

    private final @NotNull Interpreter<K> kInterpreter;
    private final @NotNull Interpreter<V> vInterpreter;

    /**
     * Constructs a GenericHashtableToStringHashtableMapper with the given key and value interpreters.
     *
     * @param kInterpreter the interpreter for converting keys to String
     * @param vInterpreter the interpreter for converting values to String
     * @throws NullPointerException if the kInterpreter or vInterpreter parameter is null
     */
    public GenericHashtableToStringHashtableMapper(@NotNull Interpreter<K> kInterpreter, @NotNull Interpreter<V> vInterpreter) {
        this.kInterpreter = kInterpreter;
        this.vInterpreter = vInterpreter;
    }

    /**
     * Creates a new GenericHashtableToStringHashtableMapper with a parallel interpreter for keys and values of the same type.
     *
     * @param <T>          the type of the keys and values
     * @param tInterpreter the interpreter for converting keys and values to String
     * @return a GenericHashtableToStringHashtableMapper with parallel key and value interpreters
     * @throws NullPointerException if the tInterpreter parameter is null
     */
    @Contract(value = "_ -> new", pure = true)
    public static <T> @NotNull GenericHashtableToStringHashtableMapper<T, T> createParallel(final Interpreter<T> tInterpreter) {
        return new GenericHashtableToStringHashtableMapper<>(tInterpreter, tInterpreter);
    }

    /**
     * Applies the mapping function to the given input Hashtable, converting its keys and values to String
     * and creating a new Hashtable with String keys and String values.
     *
     * @param objectObjectHashtable the input Hashtable to be mapped
     * @return a new Hashtable with String keys and String values
     * @throws NullPointerException if the objectObjectHashtable parameter is null
     */
    @Override
    public @NotNull Hashtable<String, String> apply(@NotNull Hashtable<K, V> objectObjectHashtable) {
        final Hashtable<String, String> stringStringHashtable = new Hashtable<>();
        objectObjectHashtable.forEach((key, value) ->
                stringStringHashtable.put(kInterpreter.reverse(key), vInterpreter.reverse(value)));
        return stringStringHashtable;
    }
}