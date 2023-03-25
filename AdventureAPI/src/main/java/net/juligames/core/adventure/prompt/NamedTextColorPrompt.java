package net.juligames.core.adventure.prompt;

import de.bentzin.conversationlib.ConversationContext;
import de.bentzin.conversationlib.prompt.Prompt;
import de.bentzin.conversationlib.prompt.ValidatingPrompt;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
public abstract class NamedTextColorPrompt extends ValidatingPrompt {
    @Override
    protected boolean isInputValid(@NotNull ConversationContext context, @NotNull String input) {
        return NamedTextColor.NAMES.value(input) != null;
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
        return acceptTextColorInput(context, Objects.requireNonNull(NamedTextColor.NAMES.value(input)));
    }

    public abstract @Nullable Prompt acceptTextColorInput(@NotNull ConversationContext context, @NotNull NamedTextColor namedTextColor);
}
