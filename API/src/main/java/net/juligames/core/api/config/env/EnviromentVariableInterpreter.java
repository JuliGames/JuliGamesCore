package net.juligames.core.api.config.env;

import net.juligames.core.api.config.Interpreter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 07.03.2023
 */
@ApiStatus.AvailableSince("1.5")
public class EnviromentVariableInterpreter implements Interpreter<EnvironmentVariable> {
    @Override
    public @NotNull EnvironmentVariable interpret(String input) throws Exception {
        return new EnvironmentVariable(input);
    }

    @Override
    public @NotNull String reverse(@NotNull EnvironmentVariable environmentVariable) {
        return environmentVariable.key();
    }
}
