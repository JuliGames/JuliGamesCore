package net.juligames.core.paper;

import net.juligames.core.Core;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Ture Bentzin
 * 18.11.2022
 */
public class PaperCoreDebugPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Core core = new Core();
        core.start("paper-core");
    }
}
