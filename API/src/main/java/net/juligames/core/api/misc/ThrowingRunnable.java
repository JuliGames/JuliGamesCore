package net.juligames.core.api.misc;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Like a normal {@link Runnable} but it accepts Exceptions
 * @author Ture Bentzin
 * 10.04.2023
 */
public interface ThrowingRunnable {

    @Contract(pure = true)
    static @NotNull ThrowingRunnable fromRunnable(@NotNull Runnable runnable) {
        return runnable::run;
    }

    void run() throws Exception;
}
