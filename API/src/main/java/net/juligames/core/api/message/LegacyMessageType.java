package net.juligames.core.api.message;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code LegacyMessageType} interface represents the types of formatting codes used in legacy Minecraft chat messages.
 * These codes are used to change the color, formatting, and other properties of text in the chat.
 *
 * <p>The {@code LegacyMessageType} interface provides two default implementations for commonly used formatting codes:
 * {@link #AMPERSAND} and {@link #SECTION}. Additionally, custom formatting codes can be created using the static
 * {@link #custom(char)} method.
 *
 * <p>Note that these formatting codes are only supported in legacy Minecraft versions. Modern Minecraft versions use
 * JSON-formatted chat messages instead. You are still able to use them through Spigot but it is recommended to use
 * Components instead
 *
 * @since 1.4
 */
@ApiStatus.AvailableSince("1.4")
public interface LegacyMessageType {

    /**
     * A {@code LegacyMessageType} implementation for the '&' formatting code.
     */
    @NotNull LegacyMessageType AMPERSAND = () -> '&';

    /**
     * A {@code LegacyMessageType} implementation for the '§' formatting code.
     */
    @NotNull LegacyMessageType SECTION = () -> '§';

    /**
     * Creates a custom {@code LegacyMessageType} implementation for the specified character.
     *
     * @param c the formatting code character
     * @return a new {@code CustomLegacyMessageType} instance for the specified character
     */
    @Contract(pure = true)
    static @NotNull CustomLegacyMessageType custom(char c) {
        return new CustomLegacyMessageType(c);
    }

    /**
     * Gets the formatting code character for this {@code LegacyMessageType}.
     *
     * @return the formatting code character
     */
    char getChar();

    /**
     * A {@code record} implementation of {@code LegacyMessageType} for custom formatting codes.
     */
    record CustomLegacyMessageType(char c) implements LegacyMessageType {

        /**
         * Gets the formatting code character for this {@code CustomLegacyMessageType}.
         *
         * @return the formatting code character
         */
        @Override
        public char getChar() {
            return c;
        }
    }
}
