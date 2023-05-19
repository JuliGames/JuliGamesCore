package net.juligames.core.api.message;

import org.jetbrains.annotations.NotNull;

/**
 * This interface provides methods for serializing a MiniMessage to plain text or legacy format.
 * It also provides methods for translating legacy format messages to MiniMessage format.
 * @author Ture Bentzin
 * 25.12.2022
 */
public interface MiniMessageSerializer {

    /**
     * This method resolves the given message to a plain text string, stripping all colors and decorations.
     *
     * @param message the MiniMessage to resolve
     * @return a plain text string representation of the MiniMessage
     */
    @NotNull
    String resolvePlain(@NotNull Message message);

    /**
     * This method resolves the given message to a legacy format string, removing all advanced decorations.
     *
     * @param message the MiniMessage to resolve
     * @return a legacy format string representation of the MiniMessage
     */
    @Deprecated
    @NotNull
    String resolveLegacy(@NotNull Message message);

    /**
     * This method resolves the given miniMessage to a plain text string, stripping all colors and decorations.
     *
     * @param miniMessage the miniMessage to resolve
     * @return a plain text string representation of the miniMessage
     */
    @Deprecated
    @NotNull
    String resolvePlain(@NotNull String miniMessage);

    /**
     * This method resolves the given miniMessage to a legacy format string, removing all advanced decorations.
     *
     * @param miniMessage the miniMessage to resolve
     * @return a legacy format string representation of the miniMessage
     */
    @Deprecated
    @NotNull
    String resolveLegacy(@NotNull String miniMessage);

    /**
     * This method translates a legacy format string to MiniMessage format.
     *
     * @param ampersand the legacy format string to translate
     * @return a MiniMessage format string representation of the legacy format string
     */
    @NotNull String translateLegacyToMiniMessage(@NotNull String ampersand);

    /**
     * This method translates a legacy format section string to MiniMessage format.
     *
     * @param section the legacy format section string to translate
     * @return a MiniMessage format string representation of the legacy format section string
     */
    @NotNull String translateLegacySectionToMiniMessage(@NotNull String section);
}
