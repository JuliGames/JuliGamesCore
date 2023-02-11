package net.juligames.core.message;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 10.02.2023
 */
@ApiStatus.Internal
public record CoreReplacement(int index, Type type, String insertion, String pattern) implements Replacement {
    @Override
    public @Range(from = 0, to = Integer.MAX_VALUE) int getIndex() {
        return index;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Contract(pure = true)
    @Override
    public @NotNull Function<String, String> insertInstant() {
        return s -> s.replace(getAsPattern(),insertion);
    }

    @Override
    public String getInsertion() {
        return insertion;
    }

    @Override
    public String getAsPattern() {
        if(pattern.contains("" + index)) //weak check ({10} would pass if index was 0)
            return pattern;
        else throw new IllegalStateException("index " + index + " not found in pattern: " + pattern);
    }
}
