package net.juligames.core.api.config;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * This might be used later outside of this package
 *
 * @author Ture Bentzin
 * 12.04.2023
 */
@ApiStatus.Internal
@ApiStatus.AvailableSince("1.6")
public sealed interface Reverser<T> permits Interpreter, Reverser.PrivateReverser {

    /**
     * The function#apply should never return null
     *
     * @param function the function
     * @param <T>      Type
     * @return a reverser based on the function
     */
    @Contract(pure = true)
    static <T> @NotNull Reverser<T> ofFunctional(@NotNull Function<T, String> function) {
        return (PrivateReverser<T>) function::apply;
    }

    /**
     * This will return a new {@link Reverser} that will execute the {@link #reverse(Object)} method from the given
     * tReverser. This only benefits of you want to prevent casting this {@link Reverser} to a {@link Interpreter}!
     *
     * @param tReverser the reverser
     * @param <T>       T
     * @return a new reverser
     */
    @ApiStatus.Experimental
    @Contract(pure = true)
    static <T> @NotNull Reverser<T> linkOf(@NotNull Reverser<T> tReverser) {
        return ofFunctional(tReverser::reverse);
    }

    /**
     * This {@link Reverser} will reverse the given object ONLY based on its toString!
     * Should only be used with EXTREME caution!
     * This does not respect {@link CustomInterpretable}
     *
     * @param <T> T
     * @return a new reverser
     */
    @ApiStatus.Experimental
    @Contract(pure = true)
    static <T> @NotNull Reverser<T> fromToString() {
        return (PrivateReverser<T>) Object::toString;
    }

    /**
     * This {@link Reverser} will reverse the given object ONLY based on its {@link String#valueOf(Object)}!
     * Should only be used with EXTREME caution!
     * This does not respect {@link CustomInterpretable}
     *
     * @param <T> T
     * @return a new reverser
     */
    @ApiStatus.Experimental
    @Contract(pure = true)
    static <T> @NotNull Reverser<T> fromValueOf() {
        return (PrivateReverser<T>) String::valueOf;
    }

    /**
     * Converts the given instance of T according to this implementation to a String with the intention that this string
     * can be interpreted back to the instance of T with the correct implementation of {@link Interpreter} that
     * implements this {@link Reverser}.
     * It should always be possible to convert t to a string!
     * This process is known as "reversing the interpretation of a string back to the string"
     *
     * @param t the object to be reversed
     * @return the string that was reversed from the instance of T
     */
    @SuppressWarnings("override")
    @NotNull String reverse(T t);

    /**
     * Meant ONLY for use with {@link #ofFunctional(Function)}
     *
     * @param <T> the type
     */
    @ApiStatus.Internal
    @ApiStatus.Experimental
    non-sealed interface PrivateReverser<T> extends Reverser<T> {
        @Override
        public abstract @NotNull String reverse(T t);
    }
}
