package net.juligames.core.api.config;

import de.bentzin.tools.DoNotOverride;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * This interface defines methods for interpreting a string as an instance of a certain type and for reversing the
 * interpretation back to a string.
 * <p>
 * This is an example implementation for interpreting Integers
 * <pre>
 * {@code

 * public final class IntegerInterpreter implements Interpreter<Integer> {
 *
 *     @Override
 *     public Integer interpret(String input) throws Exception {
 *         return Integer.parseInt(input);
 *     }
 *
 *     @Override
 *     public String reverse(Integer integer) {
 *         return Integer.toString(integer);
 *     }
 *
 *     public Class<Integer> getType() {
 *         return Integer.class;
 *     }
 * }
 * }
 * </pre>
 *
 * @param <T> the type of object that this Interpreter can interpret
 */
public non-sealed interface Interpreter<T> extends Reverser<T>, PrimitiveInterpreter<T> {

    /**
     * Converts the given input according to this implementation to a given Object of the provided Type.
     * This process is known as "interpreting the input as an instance of Type T"
     *
     * @param input the input to be interpreted
     * @return the instance of T that results from the interpretation
     * @throws Exception will be thrown if the input can't be interpreted according to this implementation
     */
    @NotNull T interpret(final String input) throws Exception;

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
    @NotNull String reverse(T t);

    /**
     * Applies the given function to the result of {@link #reverse(T)} and returns its result.
     *
     * @param t        the object to be reversed
     * @param function the function to apply to the result of {@link #reverse(T)}
     * @param <R>      the type of the result of the function
     * @return the result of applying the function to the result of {@link #reverse(T)}
     */
    default <R> R reverseAndThen(@NotNull T t, @NotNull Function<String, R> function) {
        return function.apply(reverse(t));
    }

    /**
     * Completes the given CompletableFuture with the result of {@link #reverse(T)}.
     *
     * @param t                 the object to be reversed
     * @param completableFuture the CompletableFuture to complete with the result of {@link #reverse(T)}
     * @param <R>               the type of the CompletableFuture
     */
    @ApiStatus.AvailableSince("1.6")
    @ApiStatus.Experimental
    default <R> void reverseAndThenComplete(@NotNull T t, @NotNull CompletableFuture<String> completableFuture) {
        completableFuture.complete(reverse(t));
    }

    /**
     * Interprets the given input according to this implementation to an instance of T and completes the given
     * CompletableFuture with the result of the interpretation. This process is known as "interpreting the input as an
     * instance of Type T". If the interpretation fails, an Exception will be thrown.
     *
     * @param input              the input to be interpreted
     * @param completableFuture the CompletableFuture that should be completed with the result of the interpretation
     * @throws Exception will be thrown if the input cant be interpreted according to this implementation
     */
    @ApiStatus.AvailableSince("1.6")
    @ApiStatus.Experimental
    default <R> void interpretAndThenComplete(@NotNull String input, @NotNull CompletableFuture<T> completableFuture) throws Exception {
        completableFuture.complete(interpret(input));
    }

    /**
     * This will return a cast of this to a {@link Reverser}
     */
    @DoNotOverride
    @ApiStatus.AvailableSince("1.6")
    default Reverser<T> asReverser() {
        return this;
    }

    /**
     * This will return a cast of this to a {@link PrimitiveInterpreter}
     */
    @DoNotOverride
    @ApiStatus.AvailableSince("1.6")
    default PrimitiveInterpreter<T> asIInterpreter() {
        return this;
    }

}
