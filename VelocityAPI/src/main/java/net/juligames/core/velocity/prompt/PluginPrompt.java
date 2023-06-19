package net.juligames.core.velocity.prompt;

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import de.bentzin.conversationlib.prompt.FixedSetPrompt;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
@ApiStatus.AvailableSince("1.6")
public abstract class PluginPrompt extends FixedSetPrompt<PluginContainer> implements ProxyServerPrompt {

    private final ProxyServer proxyServer;

    public PluginPrompt(@NotNull ProxyServer proxyServer) {
        super(s -> proxyServer.getPluginManager().getPlugin(s).orElse(null),
                proxyServer.getPluginManager().getPlugins());
        this.proxyServer = proxyServer;
    }

    @Override
    public ProxyServer getProxy() {
        return proxyServer;
    }
}