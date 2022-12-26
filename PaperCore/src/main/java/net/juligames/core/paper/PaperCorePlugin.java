package net.juligames.core.paper;

import net.juligames.core.Core;
import net.juligames.core.adventure.AdventureCore;
import net.juligames.core.paper.events.ServerBootFinishedEvent;
import net.juligames.core.paper.minigame.StartCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

/**
 * @author Ture Bentzin
 * 18.11.2022
 */
public class PaperCorePlugin extends JavaPlugin {

    private AdventureCore adventureCore;
    private static PaperCorePlugin plugin;

    public static PaperCorePlugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        Core core = new Core();
        plugin = this;
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

        Bukkit.getPluginManager().registerEvents(new PaperCoreEventListener(),this);

        //Try to load miniGame (if present)

        getServer().getScheduler().scheduleSyncDelayedTask(this, () -> Bukkit.getPluginManager().callEvent(new ServerBootFinishedEvent()));

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
}
