package net.juligames.core.paper.prompt;

import de.bentzin.conversationlib.prompt.Prompt;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
public interface PluginManagerPrompt extends Prompt {

    @NotNull PluginManager getPluginManager();
}
