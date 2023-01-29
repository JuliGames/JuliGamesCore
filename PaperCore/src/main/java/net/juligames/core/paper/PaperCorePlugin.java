package net.juligames.core.paper;

import net.juligames.core.Core;
import net.juligames.core.adventure.AdventureCore;
import net.juligames.core.api.API;
import net.juligames.core.paper.events.ServerBootFinishedEvent;
import net.juligames.core.paper.minigame.StartCommand;
import net.juligames.core.paper.notification.EventNotificationListener;
import net.juligames.core.paper.perms.PermissionCheckReturn;
import net.juligames.core.paper.plugin.CorePluginLoadManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

/**
 * @author Ture Bentzin
 * 18.11.2022
 */
public class PaperCorePlugin extends JavaPlugin {

    private static PaperCorePlugin plugin;
    private AdventureCore adventureCore;
    private CorePluginLoadManager corePluginLoadManager;

    public static PaperCorePlugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        try {
            plugin = this;
            Core core = new Core();
            String serverName = Bukkit.getName() + "@" + ((Bukkit.getServer().getIp().isEmpty()) ? Bukkit.getServer().getIp() + ":" : Bukkit.getServer().getPort());
            getLogger().info("starting core with the following identification: " + serverName);
            core.start("paper-core|" + serverName);
            //start adventureAPI
            getLogger().info("starting adventureCore v." + AdventureCore.API_VERSION);
            adventureCore = new AdventureCore();
            adventureCore.start();
            core.setOnlineRecipientProvider(() -> {
                List<PaperMessageRecipient> paperMessageRecipients = new java.util.ArrayList<>(Bukkit.getOnlinePlayers().stream().map(PaperMessageRecipient::new).toList());
                paperMessageRecipients.add(new PaperMessageRecipient(Bukkit.getConsoleSender()));
                return paperMessageRecipients;
            });

            core.getCommandApi().setCommandHandler(s -> {
                Bukkit.getScheduler().runTask(this, () -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
                });
            });

            PermissionCheckReturn.registerMessages(API.get().getMessageApi());

            Objects.requireNonNull(getCommand("message")).setExecutor(new MessageCommand());
            Objects.requireNonNull(getCommand("locale")).setExecutor(new LocaleCommand());
            Objects.requireNonNull(getCommand("replacetest")).setExecutor(new ReplaceTestCommand());
            Objects.requireNonNull(getCommand("bctest")).setExecutor(new BCTestCommand());
            Objects.requireNonNull(getCommand("start")).setExecutor(new StartCommand());
            Objects.requireNonNull(getCommand("printMessageCache")).setExecutor(new PrintMessageCacheCommand());

            Bukkit.getPluginManager().registerEvents(new PaperCoreEventListener(), this);

            //Register NotificationEvent
            core.getNotificationApi().registerListener(new EventNotificationListener());

            //Try to load miniGame (if present)
            getServer().getScheduler().scheduleSyncDelayedTask(this, () -> Bukkit.getPluginManager().callEvent(new ServerBootFinishedEvent(core))); //experimental use of core

            //CorePlugin
            {
                final File file = Bukkit.getPluginsFolder();
                corePluginLoadManager = new CorePluginLoadManager(file, Bukkit.getServer());
                loadCorePlugins();
            }
        } catch (Exception e) {
            Core core = Core.getInstance();
            core.getCoreLogger().info(e + " -> Client shutdown!");
            killClient(core);
            throw new RuntimeException(e);
        }

    }

    protected void killClient(@NotNull Core core) {
        adventureCore.dropApiService();
        //Try to abort miniGame (if present)
        core.getLocalMiniGame().ifPresent(basicMiniGame -> {
            getLogger().info("aborting MiniGame: " + basicMiniGame.getPlainName());
            basicMiniGame.abort();
        });
        core.stop();
    }

    @Override
    public void onDisable() {
        Core core = Core.getInstance();
        core.getCoreLogger().info("onDisable() -> Client shutdown!");
        killClient(core);
    }

    private void loadCorePlugins() {
        if (corePluginLoadManager != null) {
            try {
                corePluginLoadManager.load(); // Experimental
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, "An issue was recorded while trying to load experimental CorePlugins: " + e.getMessage());
                e.printStackTrace();
                getLogger().log(Level.SEVERE, "Please be aware that issues regarding this system will have low priority duo to the unclear" +
                        " situation about the future of this system!");
            }

        } else {
            getLogger().log(Level.SEVERE, "cant load Plugins!");
        }
    }
}
