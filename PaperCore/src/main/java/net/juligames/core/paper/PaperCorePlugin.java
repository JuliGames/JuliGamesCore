package net.juligames.core.paper;

import net.juligames.core.Core;
import net.juligames.core.adventure.AdventureCore;
import net.juligames.core.api.API;
import net.juligames.core.paper.events.ServerBootFinishedEvent;
import net.juligames.core.paper.minigame.StartCommand;
import net.juligames.core.paper.notification.EventNotificationListener;
import net.juligames.core.paper.plugin.CorePluginLoadManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.lang.reflect.Field;
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

        Objects.requireNonNull(getCommand("message")).setExecutor(new MessageCommand());
        Objects.requireNonNull(getCommand("locale")).setExecutor(new LocaleCommand());
        Objects.requireNonNull(getCommand("replacetest")).setExecutor(new ReplaceTestCommand());
        Objects.requireNonNull(getCommand("bctest")).setExecutor(new BCTestCommand());
        Objects.requireNonNull(getCommand("start")).setExecutor(new StartCommand());

        Bukkit.getPluginManager().registerEvents(new PaperCoreEventListener(), this);

        //Register NotificationEvent
        core.getNotificationApi().registerListener(new EventNotificationListener());

        //Try to load miniGame (if present)
        getServer().getScheduler().scheduleSyncDelayedTask(this, () -> Bukkit.getPluginManager().callEvent(new ServerBootFinishedEvent(core))); //experimental use of core

        //CorePlugin
        {
            final File file = new File(Bukkit.getPluginsFolder().getParentFile(),"coreplugins"); //always parallel
            corePluginLoadManager = new CorePluginLoadManager(file, Bukkit.getServer());
            loadCorePlugins();
        }

    }

    @Override
    public void onDisable() {
        Core core = Core.getInstance();
        core.getCoreLogger().info("onDisable() -> Client shutdown!");
        adventureCore.dropApiService();
        //Try to abort miniGame (if present)
        core.getLocalMiniGame().ifPresent(basicMiniGame -> {
            getLogger().info("aborting MiniGame: " + basicMiniGame.getPlainName());
            basicMiniGame.abort();
        });
        core.stop();
    }

    private void loadCorePlugins() {
        if(corePluginLoadManager != null) {

            corePluginLoadManager.load(); //TODO

        }else {
            getLogger().log(Level.SEVERE,"cant load Plugins!");
        }
    }
}
