package net.juligames.core.adventure.api.interpreter;

import net.juligames.core.api.config.Interpreter;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * The `IndexBackedInterpreterProviderBuilder` class provides a builder pattern for creating instances of `IndexBackedInterpreterProvider`.
 *
 * @param <K> The type of the keys in the index.
 * @param <V> The type of the values in the index.
 * @param <T> The type of the index that implements the `Index` interface.
 * @author Ture Bentzin
 * 18.05.2023
 */
@ApiStatus.AvailableSince("1.6")
@ApiStatus.Experimental
public class IndexBackedInterpreterProviderBuilder<K, V, T extends Index<K, V>> {
    private Interpreter<K> kInterpreter;
    private Interpreter<V> vInterpreter;
    private Index<K, V> source;
    private boolean inverted = false;

    /**
     * Sets the interpreter for the key type `K`.
     *
     * @param kInterpreter The interpreter for the key type.
     * @return The builder instance.
     */
    public @NotNull IndexBackedInterpreterProviderBuilder<K, V, T> setKInterpreter(Interpreter<K> kInterpreter) {
        this.kInterpreter = kInterpreter;
        return this;
    }

    /**
     * Sets the interpreter for the value type `V`.
     *
     * @param vInterpreter The interpreter for the value type.
     * @return The builder instance.
     */
    public @NotNull IndexBackedInterpreterProviderBuilder<K, V, T> setVInterpreter(Interpreter<V> vInterpreter) {
        this.vInterpreter = vInterpreter;
        return this;
    }

    /**
     * Sets the data source to pull from, implementing the `Index` interface.
     *
     * @param source The data source.
     * @return The builder instance.
     */
    public @NotNull IndexBackedInterpreterProviderBuilder<K, V, T> setSource(Index<K, V> source) {
        this.source = source;
        return this;
    }

    /**
     * Sets the inversion flag, indicating whether the interpretation is inverted or not.
     *
     * @param inverted The inversion flag.
     * @return The builder instance.
     */
    public @NotNull IndexBackedInterpreterProviderBuilder<K, V, T> setInverted(boolean inverted) {
        this.inverted = inverted;
        return this;
    }

    /**
     * Creates a new instance of `IndexBackedInterpreterProvider` based on the provided configuration.
     *
     * @return The created `IndexBackedInterpreterProvider`.
     * @throws IllegalStateException If the builder does not meet the minimum criteria to create an `IndexBackedInterpreterProvider`.
     */
    public @NotNull IndexBackedInterpreterProvider<K, V, T> createIndexBackedInterpreterProvider() throws IllegalStateException {
        if (kInterpreter == null || vInterpreter == null || source == null) {
            throw new IllegalStateException("Builder does not meet the minimum criteria to create an IndexBackedInterpreterProvider!");
        }
        return new IndexBackedInterpreterProvider<>(kInterpreter, vInterpreter, source, inverted);
    }

    /**
     * Builds and supplies the appropriate `Interpreter` based on the builder configuration.
     *
     * @return The built `Interpreter`.
     * @throws IllegalStateException If the builder does not meet the minimum criteria to create an `IndexBackedInterpreterProvider` or the `get()` method of the provider throws an exception.
     */
    public @NotNull Interpreter<?> buildAndSupply() throws IllegalStateException {
        return createIndexBackedInterpreterProvider().get();
    }
}
