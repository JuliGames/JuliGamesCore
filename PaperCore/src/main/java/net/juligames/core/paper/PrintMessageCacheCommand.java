package net.juligames.core.paper;

import net.juligames.core.caching.MessageCaching;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 21.01.2023
 */
public class PrintMessageCacheCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String s = MessageCaching.messageCache().toString();
        sender.sendMessage("NATIVE: " + s);
        return true;
    }
}
