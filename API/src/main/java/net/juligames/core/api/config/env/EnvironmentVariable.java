package net.juligames.core.api.config.env;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 07.03.2023
 */
@ApiStatus.AvailableSince("1.5")
public record EnvironmentVariable(String key) implements MapPart<String> {

    public @NotNull String get() {
        return System.getenv().get(key());
    }
}
