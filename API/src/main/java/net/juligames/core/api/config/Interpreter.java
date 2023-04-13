package net.juligames.core.api.config;

import de.bentzin.tools.DoNotOverride;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public non-sealed interface Interpreter<T> extends Reverser<T>, PrimitiveInterpreter<T> {

    /**
     * Converts the given input according to this implementation to a given Object of the provided Type.
     * This process is known as "interpreting the input as an instance of Type T"
     *
     * @param input the input to be interpreted
     * @return the instance of T that results the interpretation
     * @throws Exception will be thrown if the input cant be interpreted according to this implementation
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

    default <R> R reverseAndThen(@NotNull T t, @NotNull Function<String, R> function) {
        return function.apply(reverse(t));
    }

    @ApiStatus.AvailableSince("1.6")
    @ApiStatus.Experimental
    default <R> void reverseAndThenComplete(@NotNull T t, @NotNull CompletableFuture<String> completableFuture) {
        completableFuture.complete(reverse(t));
    }

    default <R> R interpretAndThen(@NotNull String input, @NotNull Function<T, R> function) throws Exception {
        return function.apply(interpret(input));
    }

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
