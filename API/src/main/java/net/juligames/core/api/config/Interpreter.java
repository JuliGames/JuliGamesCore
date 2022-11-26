package net.juligames.core.api.config;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 26.11.2022
 * @see BuildInInterpreters
 */

public interface Interpreter<T>{
    T interpret(String input) throws MalformedURLException, ParseException;
    String reverse(T t);

     default <R> R reverseAndThen(T t, @NotNull Function<String, R> function) {
         return function.apply(reverse(t));
     }

    default <R> R interpretAndThen(String input, @NotNull Function<T, R> function) {
        return function.apply(interpret(input));
    }
}
