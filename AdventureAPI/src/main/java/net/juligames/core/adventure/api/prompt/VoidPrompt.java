package net.juligames.core.adventure.api.prompt;

import de.bentzin.conversationlib.ConversationContext;
import de.bentzin.conversationlib.prompt.Prompt;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This prompt sends an empty message
 *
 * @author Ture Bentzin
 * 14.04.2023
 */
public final class VoidPrompt implements Prompt {

    @Override
    public @NotNull ComponentLike getPromptMessage(@NotNull ConversationContext conversationContext) {
        return Component.empty();
    }

    @Override
    public boolean blocksForInput(@NotNull ConversationContext context) {
        return false;
    }

    @Override
    public @Nullable Prompt getNextPrompt(@NotNull ConversationContext conversationContext, @Nullable String input) {
        return END_OF_CONVERSATION;
    }

}
