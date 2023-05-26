package net.juligames.core.api.misc;

import de.bentzin.tools.Independent;
import de.bentzin.tools.logging.JavaLogger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * @author Ture Bentzin
 * 10.04.2023
 */
@ApiStatus.AvailableSince("1.6")
public class APIUtils {

    private APIUtils() {

    }

    /**
     * Formats a StackFrame object to obtain caller information.
     *
     * @param frame The StackFrame object representing the caller.
     * @return A formatted string containing caller information.
     */
    @Independent
    public static @NotNull String formatCaller(StackWalker.@NotNull StackFrame frame) {
        return frame.getMethodName()
                + "@"
                + frame.getClassName()
                + ":" + frame.getLineNumber()
                + (frame.getClassName().equals(frame.getFileName())
                ? " "
                : " located in " + frame.getFileName() + "[" + frame.getByteCodeIndex() + "]");
    }

    /**
     * Executes the given `ThrowingRunnable` and returns `true` if it completes without throwing an exception, or if the thrown exception is not contained in the specified collection of exceptions.
     *
     * @param runnable   the `ThrowingRunnable` to execute
     * @param exceptions a collection of exceptions that should not be counted as a failure
     * @return `true` if the `ThrowingRunnable` completes without throwing an exception or if the thrown exception is not contained in the specified collection of exceptions
     */
    @Independent
    public static boolean executedWithoutException(@NotNull ThrowingRunnable runnable, @NotNull Collection<Exception> exceptions) {
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
    @Independent
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
    @Independent
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
    @Independent
    public static boolean executedWithoutExceptionL(@NotNull Runnable runnable, @NotNull Collection<Exception> exceptions) {
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
    @Independent
    public static boolean executedWithoutExceptionL(@NotNull Runnable runnable) {
        return executedWithoutExceptionL(runnable, Collections.emptyList());
    }

    /**
     * Maps the elements of the input stream using the provided mapper function, while dropping any elements that cause an exception during mapping.
     *
     * @param <T>    the type of the input stream elements
     * @param <R>    the type of the resulting stream elements
     * @param stream the input stream to be mapped
     * @param mapper the mapping function to apply to each element of the input stream
     * @return a new stream containing the mapped elements, with any elements causing an exception during mapping dropped
     */
    @Independent
    public static <T, R> @NotNull Stream<R> mapOrDrop(@NotNull Stream<T> stream, @NotNull Function<T, R> mapper) {
        List<R> list = new ArrayList<>();
        stream.forEachOrdered(t -> {
            try {
                list.add(mapper.apply(t));
            } catch (Exception ignored) {
                // drop
            }
        });
        return list.stream();
    }

    /**
     * Creates a JavaLogger instance from a {@link Logger}.
     *
     * @param logger The {@link Logger} instance.
     * @return A {@link JavaLogger} instance.
     */
    @Contract("_ -> new")
    @Independent
    public static @NotNull JavaLogger fromUtil(Logger logger) {
        return new JavaLogger(logger.getName(), logger);
    }

    /**
     * Creates a JavaLogger instance from a logger name.
     *
     * @param name The name of the logger.
     * @return A {@link JavaLogger} instance.
     */
    @Contract("_ -> new")
    @Independent
    public static @NotNull JavaLogger fromName(String name) {
        return fromUtil(Logger.getLogger(name));
    }


}
