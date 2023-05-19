package net.juligames.core.adventure.api.prompt.msc;

import de.bentzin.conversationlib.ConversationContext;
import de.bentzin.conversationlib.prompt.Prompt;
import de.bentzin.conversationlib.prompt.ValidatingPrompt;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 19.05.2023
 */
@ApiStatus.AvailableSince("1.6")
public abstract class TransitivePrompt<T, R> extends ValidatingPrompt {

    private final @NotNull Function<ConversationContext, T> typeSupplier;
    private final @NotNull BiFunction<T, String, R> transitiveFunction;

    public TransitivePrompt(@NotNull Function<ConversationContext, T> typeSupplier, @NotNull BiFunction<T, String, R> transitiveFunction) {
        this.typeSupplier = typeSupplier;
        this.transitiveFunction = transitiveFunction;
    }

    public static <T, R> TransitivePrompt<T, R> functional(
            @NotNull Function<ConversationContext, ComponentLike> messageFunction,
            @NotNull Function<ConversationContext, T> typeSupplier,
            @NotNull BiFunction<T, String, R> transitiveFunction,
            @Nullable BiFunction<ConversationContext, R, Prompt> acceptTransitiveInputFunction
    ) {
        return new TransitivePrompt<T, R>(typeSupplier, transitiveFunction) {
            @Override
            protected @Nullable Prompt acceptTransitiveInput(@NotNull ConversationContext context, @NotNull R relation) {
                if (acceptTransitiveInputFunction == null) return null;
                return acceptTransitiveInputFunction.apply(context, relation);
            }

            @Override
            public @NotNull ComponentLike getPromptMessage(@NotNull ConversationContext conversationContext) {
                return messageFunction.apply(conversationContext);
            }
        };
    }

    @Override
    protected boolean isInputValid(@NotNull ConversationContext context, @NotNull String input) {
        try {
            final T t = supplyType(context);
            transitiveFunction.apply(t, input);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
        final T t = supplyType(context);
        R applied = transitiveFunction.apply(t, input);
        return acceptTransitiveInput(context, applied);
    }

    protected abstract @Nullable Prompt acceptTransitiveInput(@NotNull ConversationContext context, @NotNull R relation);

    public @NotNull BiFunction<T, String, R> getTransitiveFunction() {
        return transitiveFunction;
    }

    public @NotNull T supplyType(ConversationContext context) {
        return typeSupplier.apply(context);
    }
}
