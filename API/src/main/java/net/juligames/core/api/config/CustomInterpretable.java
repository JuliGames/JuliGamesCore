package net.juligames.core.api.config;

/**
 * @author Ture Bentzin
 * 05.12.2022
 * @apiNote this can be feed into SimpleInterpreter to replace {@link Object#toString()} with {@link CustomInterpretable#toInterpretableString()}
 */
public interface CustomInterpretable {
    String toInterpretableString();
}
