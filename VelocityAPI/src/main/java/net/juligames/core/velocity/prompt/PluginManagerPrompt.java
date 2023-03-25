package net.juligames.core.velocity.prompt;

import com.velocitypowered.api.plugin.PluginManager;
import de.bentzin.conversationlib.prompt.Prompt;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
public interface PluginManagerPrompt extends Prompt {

    @NotNull PluginManager getPluginManager();
}
