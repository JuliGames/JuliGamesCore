package net.juligames.core.velocity;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import net.juligames.core.adventure.api.AdventureAPI;
import net.juligames.core.adventure.api.AudienceMessageRecipient;
import net.juligames.core.api.API;
import net.juligames.core.api.message.Message;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * @author Ture Bentzin
 * 03.12.2022#
 * This is used for messaging this proxy. use {@link VelocityPlayerMessageRecipient} to message a player and {@link ServerMessageRecipient} to message a specific server
 * @deprecated see {@link ProxyServerMessageRecipient}
 */
@Deprecated
public class ProxyMessageRecipient extends AudienceMessageRecipient {
    public ProxyMessageRecipient(ConsoleCommandSource consoleCommandSource) {
        super("console", english(), consoleCommandSource);
    }

    public ProxyMessageRecipient(String name, Supplier<String> localeSupplier, ConsoleCommandSource consoleCommandSource) {
        super(name, localeSupplier, consoleCommandSource);
    }
}
