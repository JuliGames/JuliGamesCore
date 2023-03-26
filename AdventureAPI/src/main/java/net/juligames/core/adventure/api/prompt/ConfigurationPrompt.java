package net.juligames.core.adventure.api.prompt;

import de.bentzin.conversationlib.ConversationContext;
import de.bentzin.conversationlib.prompt.FixedSetPrompt;
import de.bentzin.conversationlib.prompt.Prompt;
import net.juligames.core.api.config.Configuration;
import net.juligames.core.api.config.ConfigurationAPI;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
@ApiStatus.AvailableSince("1.6")
public abstract class ConfigurationPrompt extends FixedSetPrompt<Configuration> implements ConfigurationAPIPrompt {

    private final ConfigurationAPI configurationAPI;

    public ConfigurationPrompt(@NotNull ConfigurationAPI configurationAPI) {
        super(configurationAPI::getOrCreate, configurationAPI.getAll());
        this.configurationAPI = configurationAPI;
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
        return acceptConfigurationInput(context, configurationAPI.getOrCreate(input));
    }

    public abstract Prompt acceptConfigurationInput(@NotNull ConversationContext context, @NotNull Configuration configuration);

    public ConfigurationAPI getConfigurationAPI() {
        return configurationAPI;
    }

    @Override
    public @NotNull ConfigurationAPI getConfigurationApi() {
        return configurationAPI;
    }
}
