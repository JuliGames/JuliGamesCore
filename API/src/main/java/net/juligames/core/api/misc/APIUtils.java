package net.juligames.core.api.misc;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Ture Bentzin
 * 10.04.2023
 */
public class APIUtils {

    private APIUtils() {

    }

    public static boolean executedWithoutException(@NotNull ThrowingRunnable runnable, Exception... exceptions) {
        return executedWithoutException(runnable, List.of(exceptions));
    }

    public static boolean executedWithoutException(@NotNull ThrowingRunnable runnable) {
        return executedWithoutException(runnable, Collections.emptyList());
    }

    public static boolean executedWithoutException(@NotNull ThrowingRunnable runnable, Collection<Exception> exceptions) {
        try {
            runnable.run();
            return true;
        } catch (Exception e) {
            return !exceptions.contains(e);
        }
    }

    public static boolean executedWithoutExceptionL(@NotNull Runnable runnable, Exception... exceptions) {
        return executedWithoutExceptionL(runnable, List.of(exceptions));
    }

    public static boolean executedWithoutExceptionL(@NotNull Runnable runnable) {
        return executedWithoutExceptionL(runnable, Collections.emptyList());
    }

    public static boolean executedWithoutExceptionL(@NotNull Runnable runnable, Collection<Exception> exceptions) {
        try {
            runnable.run();
            return true;
        } catch (Exception e) {
            return !exceptions.contains(e);
        }
    }
}
