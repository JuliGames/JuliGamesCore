package net.juligames.core.paper;

import net.juligames.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

/**
 * @author Ture Bentzin
 * 18.11.2022
 */
public class PaperCorePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Core core = new Core();
        core.start("paper-core");
        core.setOnlineRecipientProvider(() -> {
            List<PaperMessageRecipient> paperMessageRecipients = new java.util.ArrayList<>(Bukkit.getOnlinePlayers().stream().map(PaperMessageRecipient::new).toList());
            paperMessageRecipients.add(new PaperMessageRecipient(Bukkit.getConsoleSender()));
            return paperMessageRecipients;
        });

        Objects.requireNonNull(getCommand("message")).setExecutor(new MessageCommand());
        Objects.requireNonNull(getCommand("locale")).setExecutor(new LocaleCommand());
        Objects.requireNonNull(getCommand("replacetest")).setExecutor(new ReplaceTestCommand());
        Objects.requireNonNull(getCommand("bctest")).setExecutor(new BCTestCommand());
    }

    @Override
    public void onDisable() {
        Core.getInstance().stop();
    }
}
