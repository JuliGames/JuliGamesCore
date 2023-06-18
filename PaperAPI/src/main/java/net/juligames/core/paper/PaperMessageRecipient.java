package net.juligames.core.paper;

import net.juligames.core.adventure.api.AdventureAPI;
import net.juligames.core.api.message.Message;
import net.juligames.core.api.message.MessageRecipient;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a message recipient on a Paper server, which is capable of receiving messages
 * through the Bukkit {@link CommandSender} interface.
 *
 * @author Ture Bentzin
 * 19.11.2022
 */
public class PaperMessageRecipient implements MessageRecipient {

    private final CommandSender commandSender;

    /**
     * Constructs a new {@code PaperMessageRecipient} with the specified {@link CommandSender}.
     *
     * @param commandSender the Bukkit {@link CommandSender} for this recipient
     */
    public PaperMessageRecipient(CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    /**
     * Returns a human-readable name that defines this recipient.
     *
     * @return the name of this recipient, which is equivalent to the name of the Bukkit {@link CommandSender}
     */
    @Override
    public @NotNull String getName() {
        return commandSender.getName();
    }

    /**
     * Delivers a message to this recipient using the Adventure API.
     *
     * @param message the {@link Message} to be delivered
     */
    @Override
    public void deliver(@NotNull Message message) {
        commandSender.sendMessage(AdventureAPI.get().getAdventureTagManager().resolve(message));
    }

    /**
     * Delivers a miniMessage string to this recipient using the Adventure API.
     *
     * @param miniMessage the miniMessage to be delivered to the Bukkit {@link CommandSender}
     */
    @Override
    public void deliver(@NotNull String miniMessage) {
        Component resolve = AdventureAPI.get().getAdventureTagManager().resolve(miniMessage);
        commandSender.sendMessage(resolve);
    }

    /**
     * Returns the locale of the player associated with this recipient, if applicable.
     *
     * @return the locale string of the player, or {@code null} if the recipient is not a player
     */
    @Override
    public @Nullable String supplyLocale() {
        if (commandSender instanceof Player player) {
            return player.locale().toString();
        } else {
            return null;
        }
    }
}