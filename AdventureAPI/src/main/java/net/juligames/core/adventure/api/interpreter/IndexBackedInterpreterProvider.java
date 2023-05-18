package net.juligames.core.adventure.api.interpreter;

import net.juligames.core.api.config.Interpreter;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * The `IndexBackedInterpreterProvider` class is an implementation of the `Supplier` interface that provides an interpreter for a given index-based data source.
 * It allows interpreting values based on keys and vice versa, using two different interpreters for key and value types.
 * The interpretation can be done in the normal direction or in an inverted direction, depending on the `inverted` flag.
 *
 * @param <K> The type of the keys in the index.
 * @param <V> The type of the values in the index.
 * @param <T> The type of the index that implements the `Index` interface.
 * @author Ture Bentzin
 * 18.05.2023
 * @apiNote You can use {@link IndexBackedInterpreterProviderBuilder} to build your instance
 */
@ApiStatus.AvailableSince("1.6")
public final class IndexBackedInterpreterProvider<K, V, T extends Index<K, V>> implements Supplier<Interpreter<?>> {

    private final Interpreter<V> normalInterpreter;
    private final Interpreter<K> invertedInterpreter;
    private final Index<K, V> source;
    private final boolean inverted;

    /**
     * Creates a new `IndexBackedInterpreterProvider` with the provided key and value interpreters and data source.
     * The interpretation is set to be in the normal direction (non-inverted).
     *
     * @param kInterpreter The interpreter for the key type `K`.
     * @param vInterpreter The interpreter for the value type `V`.
     * @param source       The data source to pull from, implementing the `Index` interface.
     */
    public IndexBackedInterpreterProvider(Interpreter<K> kInterpreter, Interpreter<V> vInterpreter, Index<K, V> source) {
        this(kInterpreter, vInterpreter, source, false);
    }

    /**
     * Creates a new `IndexBackedInterpreterProvider` with the provided key and value interpreters, data source, and inversion flag.
     *
     * @param kInterpreter The interpreter for the key type `K`.
     * @param vInterpreter The interpreter for the value type `V`.
     * @param source       The data source to pull from, implementing the `Index` interface.
     * @param inverted     Flag indicating whether the interpretation is inverted or not.
     */
    public IndexBackedInterpreterProvider(Interpreter<K> kInterpreter, Interpreter<V> vInterpreter, Index<K, V> source, boolean inverted) {
        this.source = source;
        this.inverted = inverted;

        // Create the normal interpreter
        this.normalInterpreter = new Interpreter<>() {
            /**
             * Interprets the given input as a value and retrieves it from the source.
             *
             * @param input The input to interpret.
             * @return The interpreted value.
             * @throws Exception if an error occurs during interpretation or value retrieval.
             */
            @Override
            public @NotNull V interpret(String input) throws Exception {
                return getSource().valueOrThrow(kInterpreter.interpret(input));
            }

            /**
             * Reverses the interpretation by converting the value back to its corresponding key.
             *
             * @param v The value to reverse.
             * @return The reversed key as a string.
             * @throws NoSuchElementException If the key corresponding to the value is not found in the index.
             */
            @Override
            public @NotNull String reverse(V v) throws NoSuchElementException {
                try {
                    return kInterpreter.reverse(getSource().keyOrThrow(v));
                } catch (NoSuchElementException e) {
                    throw new NoSuchElementException("Key not found for the provided value.");
                }
            }
        };

        // Create the inverted interpreter
        this.invertedInterpreter = new Interpreter<>() {
            /**
             * Interprets the given input as a key and retrieves the corresponding value from the source.
             *
             * @param input The input to interpret.
             * @return The interpreted key.
             * @throws Exception if an error occurs during interpretation or key retrieval.
             */
            @Override
            public @NotNull K interpret(String input) throws Exception {
                return getSource().keyOrThrow(vInterpreter.interpret(input));
            }

            /**
             * Reverses the interpretation by converting the key back to its corresponding value.
             *
             * @param k The key to reverse.
             * @return The reversed value as a string.
             * @throws NoSuchElementException If the value corresponding to the key is not found in the index.
             */
            @Override
            public @NotNull String reverse(K k) throws NoSuchElementException {
                try {
                    return vInterpreter.reverse(getSource().valueOrThrow(k));
                } catch (NoSuchElementException e) {
                    throw new NoSuchElementException("Value not found for the provided key.");
                }
            }

        };
    }

    /**
     * Retrieves the data source associated with this interpreter provider.
     *
     * @return The index-based data source.
     */
    public Index<K, V> getSource() {
        return source;
    }

    /**
     * Returns the interpreter to use based on the inversion flag.
     *
     * @return The interpreter instance.
     */
    @Override
    public Interpreter<?> get() {
        return !inverted ? normalInterpreter : invertedInterpreter;
    }

    /**
     * Retrieves the inverted interpreter.
     *
     * @return The interpreter for inverted interpretation.
     */
    public Interpreter<K> getInvertedInterpreter() {
        return invertedInterpreter;
    }

    /**
     * Retrieves the normal (non-inverted) interpreter.
     *
     * @return The interpreter for normal interpretation.
     */
    public Interpreter<V> getNormalInterpreter() {
        return normalInterpreter;
    }

    /**
     * Indicates whether the interpretation of the `get()` method is inverted or not.
     *
     * @return `true` if the interpretation is inverted, `false` otherwise.
     */
    public boolean isInverted() {
        return inverted;
    }
}