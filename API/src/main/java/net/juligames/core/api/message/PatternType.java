/**
 * The PatternType enum represents the different types of patterns that can be used
 * to identify placeholders in messages. The enum includes information about the
 * start and end characters of the pattern, a tag identifier for the pattern,
 * and whether the pattern should be resolved.
 *
 * @author Ture Bentzin
 * @since 11.02.2023
 */
package net.juligames.core.api.message;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum PatternType {

    // Enum constants
    UNTRUSTED('{', '}', "safe", false),
    TRUSTED('[', ']', "unsafe", true);

    // Fields
    private final char start;
    private final char end;
    private final String tagIdentifier;
    private final boolean resolve;

    // Constructor
    PatternType(char start, char end, String tagIdentifier, boolean resolve) {
        this.start = start;
        this.end = end;
        this.tagIdentifier = tagIdentifier;
        this.resolve = resolve;
    }

    // Methods - why is this so EXTREME javadoced?

    /**
     * Builds a pattern string using the start and end characters and an integer index.
     *
     * @param i the integer index to use in the pattern
     * @return the pattern string
     */
    @Contract(pure = true)
    public @NotNull String buildPattern(int i) {
        return String.valueOf(start) + i + end;
    }

    /**
     * Builds a tag string using the tag identifier and an integer index.
     *
     * @param i the integer index to use in the tag
     * @return the tag string
     */
    @Contract(pure = true)
    public @NotNull String buildTag(int i) {
        return "<" + buildTagID(i) + ">";
    }

    /**
     * Builds a tag identifier string using the tag identifier and an integer index.
     *
     * @param i the integer index to use in the tag identifier
     * @return the tag identifier string
     */
    @Contract(pure = true)
    public @NotNull String buildTagID(int i) {
        return "param_" + tagIdentifier + "_" + i;
    }

    /**
     * Converts a pattern string to a tag string.
     *
     * @param target the target string to convert
     * @param index  the integer index to use in the tag
     * @return the converted tag string
     */
    public @NotNull String convertPatternToTag(@NotNull String target, int index) {
        return target.replace(buildPattern(index), buildTag(index));
    }

    /**
     * Returns whether or not the pattern should be resolved.
     *
     * @return true if the pattern should be resolved, false otherwise
     */
    public boolean shouldParse() {
        return resolve;
    }

    /**
     * Returns a string representation of the PatternType enum constant.
     *
     * @return a string representation of the PatternType enum constant
     */
    @Contract(pure = true)
    public @NotNull String toString() {
        return "PatternType: " + name() + ": \"" + start + "i" + end + "\"";
    }
}
