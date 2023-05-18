package net.juligames.core.adventure.api.interpreter;

import net.juligames.core.api.config.Interpreter;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * @author Ture Bentzin
 * 18.05.2023
 */
public class IndexBackedInterpreterProvider<K, V, T extends Index<K, V>> implements Supplier<Interpreter<?>> {

    private final Interpreter<V> normalInterpreter;
    private final Interpreter<K> invertedInterpreter;
    private final Index<K, V> source;
    private final boolean inverted;


    /**
     * Defaults to <code>false</code> for inverted. This only affects the {@link #get()} method and only matters for automatic access!
     * @param kInterpreter interpreter for the key
     * @param vInterpreter interpreter for the value
     * @param source the datasource to pull from
     */
    public IndexBackedInterpreterProvider(Interpreter<K> kInterpreter, Interpreter<V> vInterpreter, Index<K, V> source) {
        this(kInterpreter, vInterpreter, source, false);
    }

    public IndexBackedInterpreterProvider(Interpreter<K> kInterpreter, Interpreter<V> vInterpreter, Index<K, V> source, boolean inverted) {

        this.source = source;
        this.inverted = inverted;

        {
            this.normalInterpreter = new Interpreter<>() {
                @Override
                public @NotNull V interpret(String input) throws Exception {
                    return getSource().valueOrThrow(kInterpreter.interpret(input));
                }

                @Override
                public @NotNull String reverse(V v) {
                    return kInterpreter.reverse(getSource().keyOrThrow(v));
                }
            };
        }

        {
            this.invertedInterpreter = new Interpreter<>() {
                @Override
                public @NotNull K interpret(String input) throws Exception {
                    return getSource().keyOrThrow(vInterpreter.interpret(input));
                }

                @Override
                public @NotNull String reverse(K k) {
                    return vInterpreter.reverse(getSource().valueOrThrow(k));
                }
            };
        }


    }

    public Index<K, V> getSource() {
        return source;
    }

    /**
     *
     * @return the {@link Interpreter} to use!
     */
    @Override
    public Interpreter<?> get() {
        return !inverted ? normalInterpreter : invertedInterpreter;
    }

    public Interpreter<K> getInvertedInterpreter() {
        return invertedInterpreter;
    }

    public Interpreter<V> getNormalInterpreter() {
        return normalInterpreter;
    }

    /**
     * Indicates if the {@link #get()} method will return a {@link Interpreter<V>} otherwise an {@link Interpreter<K>} will be returned
     * @return if the {@link IndexBackedInterpreterProvider} acts inverted or not!
     */
    public boolean isInverted() {
        return inverted;
    }
}
