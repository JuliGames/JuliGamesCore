package net.juligames.core.minigame.api;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.TimoCloudBukkitAPI;
import cloud.timo.TimoCloud.api.TimoCloudUniversalAPI;
import cloud.timo.TimoCloud.api.objects.BaseObject;
import cloud.timo.TimoCloud.api.objects.ServerObject;
import de.bentzin.tools.logging.Logger;
import de.bentzin.tools.logging.LoggingClass;
import net.juligames.core.api.minigame.BasicMiniGame;
import net.juligames.core.api.minigame.StartType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static net.juligames.core.minigame.api.MiniGameState.FATAL;
import static net.juligames.core.minigame.api.MiniGameState.fromServer;

/**
 * @author Ture Bentzin
 * 18.12.2022
 */
@SuppressWarnings({"unused", "SameReturnValue"})
public abstract class MiniGame extends LoggingClass implements BasicMiniGame {

    private final String plainName;
    private boolean loaded = false;
    private StartType startType;

    public MiniGame(
            @NotNull final String plainName,
            @NotNull Logger parentLogger
    ) {
        super(parentLogger.adopt(plainName));
        this.plainName = plainName;
    }

    //Description
    @Override
    public final @NotNull String getPlainName() {
        return plainName;
    }

    //Events?

    /**
     * Will be called as soon as the MiniGame is prepared by the Core
     */
    protected abstract StartType onLoad();

    /**
     * This will start the MiniGame
     *
     * @return true if start was successfully and false if not
     */
    protected abstract boolean onStart();

    /**
     * Will be called to indicate that the game regardless of the current state should be stopped and prepared for shutdown
     */
    protected abstract void onAbort();

    /**
     * Will be called if the game finished criteria was meet
     */
    protected abstract void onFinish();


    /**
     * load the MiniGame
     *
     * @return a {@link StartType}: The handling of this is not guaranteed it might start whenever it wants. Its just an indication
     * on when and how you suggest to load it
     */
    @Override
    @Nullable
    public final StartType load() {
        //load
        getLogger().info("loading " + getFullDescription() + "!");
        StartType r = null;
        if (!isLoaded()) {
            try {
                r = onLoad();
                getLogger().info("finished loading of " + getFullDescription());
                getLogger().info(getFullDescription() + " suggest the following StartType: " + r.getName());
                loaded = true;
            } catch (Exception e) {
                getLogger().error("Error while loading MiniGame: " + e.getMessage() + "! Is it up to date?");
                e.printStackTrace();
                setMiniGameState(FATAL);
                abort();
            }
        }
        return r;

    }

    @Override
    public final void start() {
        //start routine
        getLogger().info("starting " + getPlainName());
        if (isLoaded()) {
            try {
                if (!onStart()) //if false -> error while starting
                    handleStartingError(Optional.empty());
                getLogger().info("started " + getPlainName());
            } catch (Exception e) {
                handleStartingError(Optional.of(e));
            }
        }
    }

    @ApiStatus.Internal
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private void handleStartingError(Optional<Throwable> throwable) {
        boolean equals = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().equals(this.getClass()); //EXPERIMENTAL
        if (!equals) {
            throw new SecurityException();
        }
        String message;
        if (throwable.isPresent()) {
            Throwable e = throwable.get();
            message = e.getMessage();
        } else {
            message = "MiniGame indicated an internal error";
        }
        getLogger().error("Error while starting MiniGame: " + message + "! Is it up to date?");
        throwable.ifPresent(Throwable::printStackTrace);
        setMiniGameState(FATAL);
        abort();
    }

    @Override
    public final void abort() {
        //ABORT
        Class<?> callerClass = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
        getLogger().warning("aborting " + getFullDescription() + " caused by: " + callerClass.getName());
        try {
            onAbort();
        } catch (Exception e) {
            getLogger().error("Error while aborting (force-shutdown) MiniGame: " + e.getMessage() + "! Is it up to date?");
            e.printStackTrace();
            setMiniGameState(FATAL);
            getLogger().error("MiniGame cant be recovered. Please contact the developer of this MiniGame!"
                    + (getDeveloperContactEmail().isPresent() ? " Contact via: " + getDeveloperContactEmail().get() : ""));
        }
    }

    public final TimoCloudUniversalAPI universalAPI() {
        return TimoCloudAPI.getUniversalAPI();
    }

    public final TimoCloudBukkitAPI bukkitAPI() {
        return TimoCloudAPI.getBukkitAPI();
    }

    public final ServerObject currentServer() {
        return bukkitAPI().getThisServer();
    }

    public final BaseObject currentBase() {
        return currentServer().getBase();
    }

    public final Optional<MiniGameState> getMiniGameState() {
        return Optional.ofNullable(fromServer(currentServer()));
    }

    public final void setMiniGameState(@NotNull MiniGameState newState) {
        newState.apply(currentServer());
    }

    @Override
    @ApiStatus.AvailableSince("1.5")
    public StartType getStartType() {
        return startType;
    }

    @Override
    public final boolean isLoaded() {
        return loaded;
    }
}
