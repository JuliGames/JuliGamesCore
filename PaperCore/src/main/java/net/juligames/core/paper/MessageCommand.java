package net.juligames.core.paper;

import net.juligames.core.Core;
import net.juligames.core.paper.perms.PermissionConditions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public class MessageCommand implements CommandExecutor {
    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (PermissionConditions.hasPermission(sender, "paper.debug").checkAndContinue()) {
            StringJoiner joiner = new StringJoiner(" ");
            for (String arg : args) {
                joiner.add(arg);
            }
            String arg = joiner.toString();

            Core.getInstance().getMessageApi().sendMessage(arg, new PaperMessageRecipient(sender));

        }
        return true;
    }
}
