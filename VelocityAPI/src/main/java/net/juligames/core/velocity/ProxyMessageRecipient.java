package net.juligames.core.velocity;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import net.juligames.core.adventure.api.AudienceMessageRecipient;

import java.util.function.Supplier;

/**
 * @author Ture Bentzin
 * 03.12.2022#
 * This is used for messaging this proxy. use {@link VelocityPlayerMessageRecipient} to message a player and {@link ServerMessageRecipient} to message a specific server
 *
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
