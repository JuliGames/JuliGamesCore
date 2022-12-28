package net.juligames.core.api.misc;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Ture Bentzin
 * 27.11.2022
 * @see java.util.function.Consumer
 */
@FunctionalInterface
public interface TriConsumer<A, B, C> {

    /**
     * Performs the given action with the tree given parameters
     */
    void consume(A a, B b, C c);

    @NotNull
    default TriConsumer<A, B, C> andThen(TriConsumer<? super A, ? super B, ? super C> after) {
        Objects.requireNonNull(after);
        return (A a, B b, C c) -> {
            consume(a, b, c);
            after.consume(a, b, c);
        };
    }
}
