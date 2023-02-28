package net.juligames.core.api.config;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 26.11.2022
 * @see BuildInInterpreters
 */

public interface Interpreter<T> {
    @NotNull T interpret(final String input) throws Exception;

    @NotNull String reverse(T t);

    default <R> R reverseAndThen(@NotNull T t, @NotNull Function<String, R> function) {
        return function.apply(reverse(t));
    }

    default <R> R interpretAndThen(@NotNull String input, @NotNull Function<T, R> function) throws Exception {
        return function.apply(interpret(input));
    }
}
