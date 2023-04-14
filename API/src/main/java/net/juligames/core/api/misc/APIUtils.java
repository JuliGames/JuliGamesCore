package net.juligames.core.api.misc;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Ture Bentzin
 * 10.04.2023
 */
@ApiStatus.AvailableSince("1.6")
public class APIUtils {

    private APIUtils() {

    }

    /**
     * Executes the given `ThrowingRunnable` and returns `true` if it completes without throwing an exception, or if the thrown exception is not contained in the specified collection of exceptions.
     *
     * @param runnable   the `ThrowingRunnable` to execute
     * @param exceptions a collection of exceptions that should not be counted as a failure
     * @return `true` if the `ThrowingRunnable` completes without throwing an exception or if the thrown exception is not contained in the specified collection of exceptions
     */
    public static boolean executedWithoutException(@NotNull ThrowingRunnable runnable, Collection<Exception> exceptions) {
        try {
            runnable.run();
            return true;
        } catch (Exception e) {
            return !exceptions.contains(e);
        }
    }

    /**
     * Executes the given `ThrowingRunnable` and returns `true` if it completes without throwing an exception.
     *
     * @param runnable the `ThrowingRunnable` to execute
     * @return `true` if the `ThrowingRunnable` completes without throwing an exception
     */
    public static boolean executedWithoutException(@NotNull ThrowingRunnable runnable) {
        return executedWithoutException(runnable, Collections.emptyList());
    }

    /**
     * Executes the given `ThrowingRunnable` and returns `true` if it completes without throwing an exception that is contained in the specified array of exceptions.
     *
     * @param runnable   the `ThrowingRunnable` to execute
     * @param exceptions an array of exceptions that should not be counted as a failure
     * @return `true` if the `ThrowingRunnable` completes without throwing an exception or if the thrown exception is not contained in the specified array of exceptions
     */
    public static boolean executedWithoutException(@NotNull ThrowingRunnable runnable, Exception... exceptions) {
        return executedWithoutException(runnable, List.of(exceptions));
    }

    /**
     * Executes the given `Runnable` and returns `true` if it completes without throwing an exception, or if the thrown exception is not contained in the specified collection of exceptions.
     *
     * @param runnable   the `Runnable` to execute
     * @param exceptions a collection of exceptions that should not be counted as a failure
     * @return `true` if the `Runnable` completes without throwing an exception or if the thrown exception is not contained in the specified collection of exceptions
     */
    public static boolean executedWithoutExceptionL(@NotNull Runnable runnable, Collection<Exception> exceptions) {
        try {
            runnable.run();
            return true;
        } catch (Exception e) {
            return !exceptions.contains(e);
        }
    }

    /**
     * Executes the given `Runnable` and returns `true` if it completes without throwing an exception.
     *
     * @param runnable the `Runnable` to execute
     * @return `true` if the `Runnable` completes without throwing an exception
     */
    public static boolean executedWithoutExceptionL(@NotNull Runnable runnable) {
        return executedWithoutExceptionL(runnable, Collections.emptyList());
    }
}
