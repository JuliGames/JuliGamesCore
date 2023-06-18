package net.juligames.core.paper.conversation;

import net.juligames.core.Core;
import net.juligames.core.paper.PaperConversationManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
public class ConversationListener implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onChat(@NotNull AsyncPlayerChatEvent event) {
        //Player
        if (PaperConversationManager.getInstance().handleInput(event.getPlayer(), event.getMessage())) {
            Core.getInstance().getCoreLogger().debug("handled input for conversing: " + event.getPlayer().getName() + ": " + event.getMessage());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommand(@NotNull ServerCommandEvent serverCommandEvent) {
        if (PaperConversationManager.getInstance().handleInput(serverCommandEvent.getSender(),
                serverCommandEvent.getCommand())) {
            serverCommandEvent.setCancelled(true);
        }
    }

}
