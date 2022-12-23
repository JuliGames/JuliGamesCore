package net.juligames.core.paper.events;

import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerEvent;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 23.12.2022
 */
public class ServerBootFinishedEvent extends ServerEvent {
    private static final HandlerList handlerList = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
