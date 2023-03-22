package net.juligames.core.api.config.mapbacked;

import net.juligames.core.api.config.Interpreter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * 07.03.2023
 * @apiNote This can be used as a standalone class for accessing environment variables!
 */
@ApiStatus.AvailableSince("1.5")
public record EnvironmentVariable(String key) implements MapPart<String> {

    public @NotNull String get() {
        return System.getenv().get(key());
    }

    public <T> @Nullable T get(@NotNull Interpreter<T> tInterpreter) {
        try {
            return tInterpreter.interpret(get());
        } catch (Exception e) {
            return null;
        }
    }
}
