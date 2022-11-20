package net.juligames.core.paper;

import net.juligames.core.Core;
import net.juligames.core.api.message.Message;
import net.juligames.core.api.message.MessageRecipient;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public class PaperMessageRecipient implements MessageRecipient {

    private CommandSender commandSender;

    public PaperMessageRecipient(CommandSender commandSender){
        this.commandSender = commandSender;
    }

    /**
     * @return A human-readable name that defines this recipient
     */
    @Override
    public String getName() {
        return commandSender.getName();
    }

    @Override
    public void deliver(Message message) {
        commandSender.sendMessage(Core.getInstance().getMessageApi().getTagManager().resolve(message));
    }

    /**
     * delivers a miniMessage string to the recipient
     *
     * @param miniMessage
     */
    @Override
    public void deliver(String miniMessage) {
        Component resolve = Core.getInstance().getMessageApi().getTagManager().resolve(miniMessage);
        commandSender.sendMessage(resolve);
    }

    @Override
    public @Nullable String supplyLocale() {
        if(commandSender instanceof Player player) {
            return player.locale().toString();
        }
        else
            return null;
    }
}
