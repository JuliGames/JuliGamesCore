package net.juligames.core.api.config;

import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 05.12.2022
 * @apiNote this can be feed into SimpleInterpreter to replace {@link Object#toString()} with {@link CustomInterpretable#toInterpretableString()}
 */
public interface CustomInterpretable {
    @NotNull String toInterpretableString();
}
