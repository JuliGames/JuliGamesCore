package net.juligames.core.api.message;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 11.02.2023
 */
public enum PatternType {

    UNTRUSTED('{', '}', "safe", false),
    TRUSTED('[', ']', "unsafe", true);


    private final char start;
    private final char end;
    private final String tagIdentifier;
    private final boolean resolve;

    PatternType(char start, char end, String tagIdentifier, boolean parse) {
        this.start = start;
        this.end = end;
        this.tagIdentifier = tagIdentifier;
        this.resolve = parse;
    }

    @Contract(pure = true)
    public @NotNull String buildPattern(int i) {
        return String.valueOf(start) + i + end;
    }

    @Contract(pure = true)
    public @NotNull String buildTag(int i) {
        return "<" + buildTagID(i) + ">";
    }

    @Contract(pure = true)
    public @NotNull String buildTagID(int i) {
        return "param_" + tagIdentifier + "_" + i;
    }

    public @NotNull String convertPatternToTag(@NotNull String target, int index) {
        return target.replace(buildPattern(index), buildTag(index));
    }

    public boolean shouldParse() {
        return resolve;
    }

    @Contract(pure = true)
    public @NotNull String toString() {
        return "PatternType: " + name() + ": \"" + start + "i" + end + "\"";
    }
}
