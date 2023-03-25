package net.juligames.core.paper.prompt;

import de.bentzin.conversationlib.prompt.FixedSetPrompt;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
public abstract class PlayerPrompt extends FixedSetPrompt<Player> implements ServerPrompt {
    private final Server server;

    public PlayerPrompt(@NotNull Server server) {
        super(server::getPlayer, Collections.unmodifiableCollection(server.getOnlinePlayers()));
        this.server = server;
    }

    @Override
    public Server getServer() {
        return server;
    }
}
