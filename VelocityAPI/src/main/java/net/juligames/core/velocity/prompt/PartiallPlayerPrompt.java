package net.juligames.core.velocity.prompt;

import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.bentzin.conversationlib.prompt.FixedSetPrompt;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
public abstract class PartiallPlayerPrompt extends FixedSetPrompt<Collection<Player>> implements ProxyServerPrompt, PluginManagerPrompt {

    private final ProxyServer proxyServer;

    public PartiallPlayerPrompt(@NotNull ProxyServer proxyServer) {
        super(s -> Collections.singleton(proxyServer.matchPlayer(s).stream().findFirst().orElse(null)),
                proxyServer.getAllPlayers());
        this.proxyServer = proxyServer;
    }

    @Override
    public ProxyServer getProxy() {
        return proxyServer;
    }

    @Override
    public @NotNull PluginManager getPluginManager() {
        return getProxy().getPluginManager();
    }
}
