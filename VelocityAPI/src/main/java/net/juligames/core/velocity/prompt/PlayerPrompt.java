package net.juligames.core.velocity.prompt;

import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.bentzin.conversationlib.prompt.FixedSetPrompt;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
@ApiStatus.AvailableSince("1.6")
public abstract class PlayerPrompt extends FixedSetPrompt<Player> implements ProxyServerPrompt, PluginManagerPrompt {

    private final ProxyServer proxyServer;

    public PlayerPrompt(@NotNull ProxyServer proxyServer) {
        super(s -> proxyServer.getPlayer(s).orElse(null), proxyServer.getAllPlayers());
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
