package net.juligames.core.api.config;

import de.bentzin.tools.DoNotOverride;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 26.11.2022
 * @see BuildInInterpreters
 */

public non-sealed interface Interpreter<T> extends Reverser<T>, PrimitiveInterpreter<T> {
    @NotNull T interpret(final String input) throws Exception;

    @NotNull String reverse(T t);

    default <R> R reverseAndThen(@NotNull T t, @NotNull Function<String, R> function) {
        return function.apply(reverse(t));
    }

    default <R> R interpretAndThen(@NotNull String input, @NotNull Function<T, R> function) throws Exception {
        return function.apply(interpret(input));
    }

    @DoNotOverride
    default Reverser<T> asReverser() {
        return this;
    }

    @DoNotOverride
    default PrimitiveInterpreter<T> asIInterpreter() {
        return this;
    }

}
