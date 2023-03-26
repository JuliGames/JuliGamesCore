package net.juligames.core.adventure.api.prompt;

import de.bentzin.conversationlib.ConversationContext;
import de.bentzin.conversationlib.prompt.Prompt;
import net.juligames.core.adventure.AdventureTagManager;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
@ApiStatus.AvailableSince("1.6")
public abstract class MiniMessagePrompt implements Prompt {

    private final @NotNull AdventureTagManager adventureTagManager;

    protected MiniMessagePrompt(@NotNull AdventureTagManager adventureTagManager) {
        this.adventureTagManager = adventureTagManager;
    }


    @Override
    public boolean blocksForInput(@NotNull ConversationContext context) {
        return true;
    }

    @Override
    public @Nullable Prompt getNextPrompt(@NotNull ConversationContext conversationContext, @Nullable String input) {
        if (input != null) {
            return acceptComponentInput(conversationContext, adventureTagManager.resolve(input));
        }
        return acceptComponentInput(conversationContext, null);
    }

    public abstract @Nullable Prompt acceptComponentInput(@NotNull ConversationContext conversationContext, @Nullable Component component);
}

