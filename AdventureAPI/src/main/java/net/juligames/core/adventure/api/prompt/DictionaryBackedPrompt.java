package net.juligames.core.adventure.api.prompt;

import de.bentzin.conversationlib.ConversationContext;
import de.bentzin.conversationlib.prompt.Prompt;
import de.bentzin.conversationlib.prompt.ValidatingPrompt;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Ture Bentzin
 * 10.04.2023
 */
@ApiStatus.AvailableSince("1.6")
public abstract class DictionaryBackedPrompt<V> extends ValidatingPrompt {

    private final Dictionary<String, V> dictionary;

    public DictionaryBackedPrompt(Dictionary<String, V> index) {
        this.dictionary = index;
    }

    @Override
    protected boolean isInputValid(@NotNull ConversationContext context, @NotNull String input) {
        return Collections.list(dictionary.keys()).contains(input);
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
        return acceptIndexBackedInput(context, Objects.requireNonNull(dictionary.get(input)));
    }

    public abstract @Nullable Prompt acceptIndexBackedInput(@NotNull ConversationContext context, @NotNull V v);

    @Override
    protected @Nullable Component getFailedValidationText(@NotNull ConversationContext context, @NotNull String invalidInput) {
        Stream<String> stringStream = Collections.list(dictionary.keys()).stream().filter(s -> s.contains(invalidInput));
        return Component.text(" -> " + stringStream.toList() + "?");
    }
}
