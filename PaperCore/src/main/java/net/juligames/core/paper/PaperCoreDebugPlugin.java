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
public class PaperCoreDebugPlugin extends JavaPlugin {

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
    }

    @Override
    public void onDisable() {
        Core.getInstance().stop();
    }
}
