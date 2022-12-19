package net.juligames.core.api.minigame;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Ture Bentzin
 * 19.12.2022
 */
public interface BasicMiniGame {
    //Description
    @NotNull
    String getPlainName();
    @NotNull
    String getVersion();
    @NotNull
    String getDeveloperName();
    @NotNull
    Optional<String> getDeveloperContactEmail();

    default String getDescription() {
        return getPlainName() + "v. " + getVersion();
    }

    default String getFullDescription() {
        return getPlainName() + "v. " + getVersion() + " by " + getDeveloperName();
    }

    /**
     * load the MiniGame
     */
    void load();

    /**
     * start the MiniGame
     */
    void start();

    /**
     * abort the MiniGame
     */
    void abort();

    /**
     *
     * @return true if the game is still running and not finished, in lobby or loading
     */
    boolean isRunning();

    /**
     *
     * @return true if the game has finished or was aborted
     */
    boolean isFinished();

    /**
     *
     * @return true if load() has been successfully executed
     */
    boolean isLoaded();
}
