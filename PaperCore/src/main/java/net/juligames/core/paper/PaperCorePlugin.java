package net.juligames.core.paper;

import net.juligames.core.Core;
import net.juligames.core.adventure.AdventureCore;
import net.juligames.core.api.API;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;
import java.util.prefs.AbstractPreferences;

/**
 * @author Ture Bentzin
 * 18.11.2022
 */
public class PaperCorePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Core core = new Core();
        String serverName = Bukkit.getName() + "@" + ((Bukkit.getServer().getIp().isEmpty())? Bukkit.getServer().getIp() + ":": Bukkit.getServer().getPort());
        getLogger().info("stating core with the following identification: " + serverName);
        core.start("paper-core|" + serverName);
        //start adventureAPI
        getLogger().info("starting adventureCore v." + AdventureCore.API_VERSION);
        AdventureCore adventureCore = new AdventureCore();
        adventureCore.start();
        core.setOnlineRecipientProvider(() -> {
            List<PaperMessageRecipient> paperMessageRecipients = new java.util.ArrayList<>(Bukkit.getOnlinePlayers().stream().map(PaperMessageRecipient::new).toList());
            paperMessageRecipients.add(new PaperMessageRecipient(Bukkit.getConsoleSender()));
            return paperMessageRecipients;
        });

        Objects.requireNonNull(getCommand("message")).setExecutor(new MessageCommand());
        Objects.requireNonNull(getCommand("locale")).setExecutor(new LocaleCommand());
        Objects.requireNonNull(getCommand("replacetest")).setExecutor(new ReplaceTestCommand());
        Objects.requireNonNull(getCommand("bctest")).setExecutor(new BCTestCommand());

        //PaperAPI
    }

    @Override
    public void onDisable() {
        Core.getInstance().getCoreLogger().info("onDisable() -> Client shutdown!");
        Core.getInstance().stop();
    }
}
