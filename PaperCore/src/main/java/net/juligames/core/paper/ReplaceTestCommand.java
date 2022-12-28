package net.juligames.core.paper;

import net.juligames.core.api.API;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 20.11.2022
 */
public class ReplaceTestCommand implements CommandExecutor {


    private final String messageKey;

    public ReplaceTestCommand() {
        messageKey = "papercore.test.replace";
        API.get().getMessageApi().registerMessage(messageKey, "<prefix><info_color>input: {0}, {1}, {3}");
    }

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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        API.get().getMessageApi().sendMessage(messageKey,
                new PaperMessageRecipient(sender), args);

        return true;
    }
}
