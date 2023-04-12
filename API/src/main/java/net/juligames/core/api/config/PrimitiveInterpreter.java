package net.juligames.core.api.config;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * This might be used later outside of this package
 * @author Ture Bentzin
 * 12.04.2023
 */
@ApiStatus.Internal
@ApiStatus.AvailableSince("1.6")
public sealed interface PrimitiveInterpreter<T> permits Interpreter{

    /**
     * Converts the given input according to this implementation to a given Object of the provided Type.
     * This process is known as "interpreting the input as an instance of Type T"
     * @param input the input to be interpreted
     * @return the instance of T that results the interpretation
     * @throws Exception will be thrown if the input cant be interpreted according to this implementation
     */
    @SuppressWarnings("override")
    @NotNull T interpret(final String input) throws Exception;
}
