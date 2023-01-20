package net.juligames.core.api.config;

import org.jetbrains.annotations.ApiStatus;

import java.util.Iterator;

/**
 * Interpreter implementing this will be able to interpret {@link Iterator}s in regard of their order
 *
 * @param <I> The Iterable
 * @param <T> Type to iterate
 * @author Ture Bentzin
 * 10.01.2023
 */
@ApiStatus.Internal
@ApiStatus.Experimental
public interface IterableInterpreter<T, I extends Iterable<T>> extends Interpreter<I> {
}
