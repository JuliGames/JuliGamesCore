package net.juligames.core.paper;

import net.juligames.core.caching.MessageCaching;
import net.juligames.core.paper.perms.PermissionConditions;
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
        if (PermissionConditions.hasPermission(sender, "paper.debug").checkAndContinue()) {
            String s = MessageCaching.messageCache().asMap().toString();
            sender.sendMessage("NATIVE: " + s);
        }
        return true;
    }
}
