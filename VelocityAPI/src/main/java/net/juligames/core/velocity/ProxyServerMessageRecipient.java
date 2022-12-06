package net.juligames.core.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import net.juligames.core.adventure.api.AudienceMessageRecipient;

import java.util.function.Supplier;

/**
 * @author Ture Bentzin
 * 03.12.2022
 */
public class ProxyServerMessageRecipient extends AudienceMessageRecipient {
    public ProxyServerMessageRecipient(ProxyServer proxyServer) {
        super(proxyServer.getVersion().getName(), english(), proxyServer);
    }

    public ProxyServerMessageRecipient(String name, Supplier<String> localeSupplier,
                                       ProxyServer proxyServer) {
        super(name, localeSupplier, proxyServer);
    }
}
