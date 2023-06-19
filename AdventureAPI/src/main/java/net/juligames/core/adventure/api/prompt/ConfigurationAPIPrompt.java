package net.juligames.core.adventure.api.prompt;

import de.bentzin.conversationlib.prompt.Prompt;
import net.juligames.core.api.config.ConfigurationAPI;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
@ApiStatus.AvailableSince("1.6")
public interface ConfigurationAPIPrompt extends Prompt {

    @NotNull ConfigurationAPI getConfigurationApi();
}
