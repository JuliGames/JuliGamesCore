package net.juligames.core.adventure.api.prompt;

import de.bentzin.conversationlib.ConversationContext;
import de.bentzin.conversationlib.prompt.Prompt;
import de.bentzin.conversationlib.prompt.ValidatingPrompt;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
@ApiStatus.AvailableSince("1.6")
public abstract class MapBackedPrompt<V> extends ValidatingPrompt {

    private final Map<String, V> map;

    public MapBackedPrompt(@NotNull Map<String, V> map) {
        this.map = map;
    }

    @Override
    protected boolean isInputValid(@NotNull ConversationContext context, @NotNull String input) {
        return map.containsKey(input);
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
        return acceptIndexBackedInput(context, Objects.requireNonNull(map.get(input)));
    }

    public abstract @Nullable Prompt acceptIndexBackedInput(@NotNull ConversationContext context, @NotNull V v);

    @Override
    protected @Nullable Component getFailedValidationText(@NotNull ConversationContext context, @NotNull String invalidInput) {
        Stream<String> stringStream = map.keySet().stream().filter(s -> s.contains(invalidInput));
        return Component.text(" -> " + stringStream.toList() + "?");
    }
}
