package net.juligames.core.paper;

import de.bentzin.tools.logging.Logger;
import net.juligames.core.Core;
import net.juligames.core.api.API;
import net.juligames.core.paper.events.ServerBootFinishedEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Ture Bentzin
 * 23.12.2022
 */
public class PaperCoreEventListener implements Listener {

    @EventHandler
    public void onBoot(ServerBootFinishedEvent event) {
        Logger logger = API.get().getAPILogger().adopt("boot");
        logger.info("Server start finished: running post-start utilities");

        API.get().getLocalMiniGame().ifPresent(basicMiniGame -> {
            logger.info("detected MiniGame: " + basicMiniGame.getPlainName());
            basicMiniGame.load();
        });
        final ArrayList<Plugin> apis = new ArrayList<>();
        final String apiVersion = PaperCorePlugin.getPlugin().getDescription().getAPIVersion();

        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin.getDescription().getDepend().contains("PaperCore")) {
                apis.add(plugin);
                if(!Objects.equals(plugin.getDescription().getAPIVersion(), apiVersion)) {
                    plugin.getLogger().warning("Nag Author! " + Core.getFullCoreName() + " requires api-version " + apiVersion + " but " +
                            "im not smart enough to set the right api-version...");
                }
            }
        }

        logger.info("Detected plugins depending on core: " + apis);
    }

}
