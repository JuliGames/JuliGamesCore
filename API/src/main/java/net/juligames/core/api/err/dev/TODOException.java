package net.juligames.core.api.err.dev;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 16.11.2022
 * @apiNote should be used in coexistence with @{@link net.juligames.core.api.TODO}
 */
public final class TODOException extends RuntimeException{

    @Contract(pure = true)
    @Override
    public @NotNull String getMessage() {
        return "not implemented yet";
    }
}
