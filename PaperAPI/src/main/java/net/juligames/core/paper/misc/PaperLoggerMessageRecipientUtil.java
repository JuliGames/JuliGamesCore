package net.juligames.core.paper.misc;

import net.juligames.core.adventure.api.AdventureAPI;
import net.juligames.core.api.misc.LoggerMessageRecipient;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 15.03.2023
 */
@ApiStatus.AvailableSince("1.5")
public class PaperLoggerMessageRecipientUtil {

    private PaperLoggerMessageRecipientUtil() {

    }

    @ApiStatus.AvailableSince("1.5")
    public static @NotNull LoggerMessageRecipient getMessageRecipient(@NotNull Plugin plugin) {
        return new LoggerMessageRecipient(new PluginLogger(plugin.getName(), plugin),
                AdventureAPI.get().getAdventureTagManager());
    }
}
