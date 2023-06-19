package net.juligames.core.velocity.prompt;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.bentzin.conversationlib.prompt.FixedSetPrompt;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
@ApiStatus.AvailableSince("1.6")
public abstract class ServerPrompt extends FixedSetPrompt<RegisteredServer> implements ProxyServerPrompt {

    private final ProxyServer proxyServer;

    public ServerPrompt(@NotNull ProxyServer proxyServer) {
        super(s -> proxyServer.getServer(s).orElse(null), proxyServer.getAllServers());
        this.proxyServer = proxyServer;
    }

    @Override
    public ProxyServer getProxy() {
        return proxyServer;
    }
}
