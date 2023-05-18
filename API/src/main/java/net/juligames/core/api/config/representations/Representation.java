package net.juligames.core.api.config.representations;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * The `Representation` interface defines a representation of a value of type `T`.
 *
 * @param <T> The type of the value to represent.
 * @author Ture Bentzin
 * 17.04.2023
 */
@ApiStatus.AvailableSince("1.6")
@FunctionalInterface
public interface Representation<T> {

    /**
     * Creates a representation of the provided value.
     *
     * @param t   The value to represent.
     * @param <T> The type of the value.
     * @return The representation of the value.
     */
    static <T> @NotNull Representation<T> of(T t) {
        return () -> t;
    }

    /**
     * Creates a representation based on the provided supplier.
     *
     * @param supplier The supplier that provides the value.
     * @param <T>      The type of the value.
     * @return The representation based on the supplier.
     */
    static <T> @NotNull Representation<T> of(@NotNull Supplier<T> supplier) {
        return supplier::get;
    }

    /**
     * Retrieves the represented value.
     *
     * @return The represented value.
     */
    T represent();
}

