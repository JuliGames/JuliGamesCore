package net.juligames.core.paper.prompt;

import de.bentzin.conversationlib.prompt.FixedSetPrompt;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
public abstract class PluginPrompt extends FixedSetPrompt<Plugin> implements PluginManagerPrompt {
    private final PluginManager pluginManager;

    public PluginPrompt(@NotNull PluginManager pluginManager) {
        super(pluginManager::getPlugin, pluginManager.getPlugins());
        this.pluginManager = pluginManager;
    }

    @Override
    public @NotNull PluginManager getPluginManager() {
        return pluginManager;
    }
}