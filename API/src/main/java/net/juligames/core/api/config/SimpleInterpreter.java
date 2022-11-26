package net.juligames.core.api.config;

import org.jetbrains.annotations.NotNull;

/**
 * @apiNote this will default to {@link Object#toString()}
 * @author Ture Bentzin
 * 26.11.2022
 */
@FunctionalInterface
public interface SimpleInterpreter<T> extends Interpreter<T> {

    @Override
    default String reverse(@NotNull T t) {
        return t.toString();
    }
}
