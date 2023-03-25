package net.juligames.core.adventure.prompt;

import de.bentzin.conversationlib.ConversationContext;
import de.bentzin.conversationlib.prompt.Prompt;
import de.bentzin.conversationlib.prompt.ValidatingPrompt;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
@ApiStatus.AvailableSince("1.6")
public abstract class IndexBackedPrompt<V> extends ValidatingPrompt {

    private final Index<String, V> index;

    public IndexBackedPrompt(@NotNull Index<String, V> index) {
        this.index = index;
    }

    @Override
    protected boolean isInputValid(@NotNull ConversationContext context, @NotNull String input) {
        return index.keys().contains(input);
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
        return acceptIndexBackedInput(context, Objects.requireNonNull(index.value(input)));
    }

    public abstract @Nullable Prompt acceptIndexBackedInput(@NotNull ConversationContext context, @NotNull V v);

    @Override
    protected @Nullable Component getFailedValidationText(@NotNull ConversationContext context, @NotNull String invalidInput) {
        Stream<String> stringStream = index.keys().stream().filter(s -> s.contains(invalidInput));
        return Component.text(" -> " + stringStream.toList() + "?");
    }
}
