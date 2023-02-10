package net.juligames.core.api.message;

import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 25.12.2022
 */
public interface MiniMessageSerializer {

    /**
     * This will resolve the given message to a plain String. This will strip all colors and decorations!
     *
     * @param message the message
     * @return plain text
     */
    @NotNull
    String resolvePlain(@NotNull Message message);

    /**
     * This will resolve the given message to a "legacy format String". This will remove all advanced decorations!
     *
     * @param message the message
     * @return legacy message
     */
    @Deprecated
    @NotNull
    String resolveLegacy(@NotNull Message message);

    /**
     * This will resolve the given message to a plain String. This will strip all colors and decorations!
     *
     * @param miniMessage the message
     * @return plain text
     */
    @Deprecated
    @NotNull
    String resolvePlain(@NotNull String miniMessage);

    /**
     * This will resolve the given message to a "legacy format String". This will remove all advanced decorations!
     *
     * @param miniMessage the message
     * @return legacy message
     */
    @Deprecated
    @NotNull
    String resolveLegacy(@NotNull String miniMessage);
}
