package net.juligames.core.velocity;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.juligames.core.adventure.api.AudienceMessageRecipient;

import java.util.function.Supplier;

/**
 * @author Ture Bentzin
 * 03.12.2022
 */
public class ServerMessageRecipient extends AudienceMessageRecipient {
    public ServerMessageRecipient(RegisteredServer server) {
        super(server.getServerInfo().getName(),english(),server);
    }
    public ServerMessageRecipient(String name, Supplier<String> locale, RegisteredServer server) {
        super(name,locale,server);
    }
}
