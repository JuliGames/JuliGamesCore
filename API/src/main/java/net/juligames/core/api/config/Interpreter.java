package net.juligames.core.api.config;

import org.jetbrains.annotations.NotNull;
import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 26.11.2022
 * @see BuildInInterpreters
 */

public interface Interpreter<T>{
    T interpret(final String input) throws Exception;
    String reverse(T t);

     default <R> R reverseAndThen(T t, @NotNull Function<String, R> function) {
         return function.apply(reverse(t));
     }

    default <R> R interpretAndThen(String input, @NotNull Function<T, R> function) throws Exception {
        return function.apply(interpret(input));
    }
}
