package net.juligames.core.paper.misc;

import de.bentzin.tools.logging.JavaLogger;
import de.bentzin.tools.logging.Logger;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * @apiNote uses the {@link JavaLogger} and does not store the plugin. The {@link java.util.logging.Logger} that is present
 * at the moment of the execution of the constructor is used! In the case that your logger needs to be dynamic, i suggest
 * building your own Logger implementation!
 * @author Ture Bentzin
 * 11.03.2023
 * @see net.juligames.core.util.ConsumingLogger; (only present with Core)
 */
 @SuppressWarnings("JavadocReference")
 @ApiStatus.AvailableSince("1.5")
public class PluginLogger extends JavaLogger {

    public PluginLogger(String name, @NotNull Logger parent, @NotNull Plugin plugin) {
        super(name, parent, plugin.getLogger());
    }

    public PluginLogger(String name, @NotNull Plugin plugin) {
        super(name, plugin.getLogger());
    }
}
