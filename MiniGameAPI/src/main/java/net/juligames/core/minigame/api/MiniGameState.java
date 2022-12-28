package net.juligames.core.minigame.api;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.objects.ServerObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author Ture Bentzin
 * 19.12.2022
 */
@SuppressWarnings("unused")
public enum MiniGameState {
    /**
     * The server is in a general Lobby phase. The Game has not started yet
     */
    LOBBY(),
    /**
     * The server has players online. The MiniGame waits for more players to be teamed
     */
    TEAMING(),
    /**
     * The server has enough players online to start. The server is still in a "lobby" phase and counting down for
     * the start of the MiniGame
     */
    STARTING("MG_STARTING"),
    /**
     * The server is loading the MiniGame. The players are online and teamed.
     * The MiniGame does not allow new joins during this phase.
     */
    LOADING(),
    /**
     * The MiniGame is running. New players can join as spectators
     */
    INGAME(),
    /**
     * The MiniGame is running. New players can join into the running game
     */
    INGAME_OPEN(),
    /**
     * The MiniGame was finished and a winner (if present) was already selected. New players cant join the server in this phase.
     */
    FINISHED(),
    /**
     * The MiniGame was interrupted. New players can only (if possible) join as spectators. The Game-mechanics are frozen
     */
    PAUSED(),
    /**
     * The MiniGame was confronted with an unexpected error. All players maybe kicked. Stats are not saved. New players cant join
     */
    FATAL("ERROR"),
    /**
     * The MiniGame was finished by non game-mechanics. Stats are not saved. New players cant join. No winner will be selected
     */
    ABORTED();

    @NotNull
    private final String state;

    MiniGameState(@NotNull String state) {
        this.state = state;
    }


    MiniGameState() {
        this.state = this.name();
    }

    @Nullable
    public static MiniGameState fromServer(@NotNull ServerObject server) {
        String state1 = server.getState();
        try {
            return valueOf(state1.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    /**
     * Set the state of the given server to this and return the old one
     *
     * @param server the server
     * @return the old state
     */
    public String apply(@NotNull ServerObject server) {
        String serverState = server.getState();
        server.setState(state.toUpperCase());
        return serverState;
    }

    /**
     * Set the state of all matching servers
     *
     * @param serverObjectPredicate predicate, all matching will be passed through to apply
     */
    public void apply(@NotNull Predicate<ServerObject> serverObjectPredicate) {
        List<ServerObject> first = TimoCloudAPI.getUniversalAPI().getServers().stream().filter(serverObjectPredicate).toList();
        for (ServerObject serverObject : first) {
            apply(serverObject);
        }
    }

    /**
     * This will change the state of the local TimoCloud Instance if present
     *
     * @return the old state
     */
    public @Nullable String applyToLocalInstance() {
        ServerObject thisServer = TimoCloudAPI.getBukkitAPI().getThisServer();
        if (thisServer != null)
            return apply(thisServer);
        return null;
    }
}
