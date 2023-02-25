package net.juligames.core.api.message;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 25.02.2023
 */
public interface LegacyMessageType{
    @NotNull LegacyMessageType AMPERSAND = () -> '&';
    @NotNull LegacyMessageType SECTION = () -> '§';

    @Contract(pure = true)
    static @NotNull CustomLegacyMessageType custom(char c) {
        return new CustomLegacyMessageType(c);
    }

    char getChar();

    record CustomLegacyMessageType(char c) implements LegacyMessageType{
        @Override
        public char getChar() {
            return c;
        }
    }
}
