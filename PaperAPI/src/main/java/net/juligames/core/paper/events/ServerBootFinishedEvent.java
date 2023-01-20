package net.juligames.core.paper.events;

import net.juligames.core.api.API;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * This event gets called when the Server first ticks after the successful boot of the PaperCore.
 * If you access the {@link net.juligames.core.api.API} here it should be safe if the caller was indeed the
 * PaperCore. With Version 1.1 for safety reasons this Event provides you a {@link #getAPI()} method that should
 * always provide a usable {@link API} and prevents misleading calls of this {@link org.bukkit.event.Event} at least
 * in the case that the {@link API} is not even present.
 *
 * @author Ture Bentzin
 * 23.12.2022
 */
public final class ServerBootFinishedEvent extends ServerEvent {
    private static final HandlerList handlerList = new HandlerList();
    private final @NotNull API api;

    public ServerBootFinishedEvent(API api) {
        this.api = api;
    }

    @Deprecated(forRemoval = true, since = "1.1")
    public ServerBootFinishedEvent() {
        this.api = API.get();
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public @NotNull API getAPI() {
        return api;
    }
}
