package net.juligames.core.minigame.api;

import de.bentzin.tools.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author Ture Bentzin
 * 19.12.2022
 */
public abstract class SimpleMiniGame extends MiniGame {

    private final String version;
    private final String developerName;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<String> developerContactEmail;

    public SimpleMiniGame(@NotNull String plainName, @NotNull String version, @NotNull String developerName, @Nullable String developerContactEmail, @NotNull Logger parentLogger) {
        super(plainName, parentLogger);
        this.version = version;
        this.developerName = developerName;
        this.developerContactEmail = Optional.ofNullable(developerContactEmail);
    }

    @Override
    public @NotNull String getVersion() {
        return version;
    }

    @Override
    public @NotNull String getDeveloperName() {
        return developerName;
    }

    @Override
    public @NotNull Optional<String> getDeveloperContactEmail() {
        return developerContactEmail;
    }

}
