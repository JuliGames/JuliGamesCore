package net.juligames.core.api.config.representations;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * @author Ture Bentzin
 * 17.04.2023
 */
@ApiStatus.AvailableSince("1.6")
@FunctionalInterface
public interface Representation<T> {

    @Contract(pure = true)
    static <T> @NotNull Representation<T> of(T t) {
        return () -> t;
    }

    @Contract(pure = true)
    static <T> @NotNull Representation<T> of(@NotNull Supplier<T> supplier) {
        return supplier::get;
    }

    T represent();
}
