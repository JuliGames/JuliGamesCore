package net.juligames.core.api.config;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * This might be used later outside of this package
 * @author Ture Bentzin
 * 12.04.2023
 */
@ApiStatus.Internal
public sealed interface Reverser<T> permits Interpreter {

    /**
     * Converts the given instance of T according to this implementation to a String with the intention that this string
     * can be interpreted back to the instance of T with the correct implementation of {@link Interpreter} that
     * implements this {@link Reverser}.
     * It should always be possible to convert t to a string!
     * This process is known as "reversing the interpretation of a string back to the string"
     * @param t the object to be reversed
     * @return the string that was reversed from the instance of T
     */
    @SuppressWarnings("override")
    @NotNull String reverse(T t);
}
