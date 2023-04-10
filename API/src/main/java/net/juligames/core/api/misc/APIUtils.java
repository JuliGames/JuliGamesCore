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

    public static boolean executedWithoutException(@NotNull Runnable runnable, Exception... exceptions) {
        return executedWithoutException(runnable, List.of(exceptions));
    }

    public static boolean executedWithoutException(@NotNull Runnable runnable) {
        return executedWithoutException(runnable, Collections.emptyList());
    }

    public static boolean executedWithoutException(@NotNull Runnable runnable, Collection<Exception> exceptions) {
        try {
            runnable.run();
            return true;
        } catch (Exception e) {
            return !exceptions.contains(e);
        }
    }
}
