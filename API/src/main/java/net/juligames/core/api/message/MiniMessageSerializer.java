package net.juligames.core.api.message;

import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 25.12.2022
 */
public interface MiniMessageSerializer {

    @NotNull
    String resolvePlain(@NotNull Message message);

    @Deprecated
    @NotNull
    String resolveLegacy(@NotNull Message message);

    @Deprecated
    @NotNull
    String resolvePlain(@NotNull String miniMessage);

    @Deprecated
    @NotNull
    String resolveLegacy(@NotNull String miniMessage);
}
