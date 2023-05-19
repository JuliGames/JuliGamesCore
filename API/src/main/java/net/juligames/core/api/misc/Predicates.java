package net.juligames.core.api.misc;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * This class provides predicates
 * @implNote might use {@link APIUtils#executedWithoutExceptionL(Runnable)} later
 * @author Ture Bentzin
 * 19.05.2023
 */
public class Predicates {

    private Predicates() {

    }

    @Contract(pure = true)
    @ApiStatus.AvailableSince("1.6")
    public static <T> @NotNull Predicate<T> instanceOf(Class<?> clazz) {
        return t -> t.getClass().isInstance(clazz);
    }

    @Contract(pure = true)
    @ApiStatus.AvailableSince("1.6")
    public static <T> @NotNull Predicate<T> nestedIn(Class<?> clazz) {
        return t -> t.getClass().getNestHost().equals(clazz);
    }

    @Contract(pure = true)
    @ApiStatus.AvailableSince("1.6")
    public static <T> @NotNull Predicate<T> executes(Consumer<T> operation) {
        return t -> {
            try {
                operation.accept(t);
            } catch (Exception e) {
                return false;
            }
            return true;
        };
    }

    @Contract(pure = true)
    @ApiStatus.AvailableSince("1.6")
    public static <T> @NotNull Predicate<T> fails(Consumer<T> operation) {
        return Predicate.not(executes(operation));
    }
}
