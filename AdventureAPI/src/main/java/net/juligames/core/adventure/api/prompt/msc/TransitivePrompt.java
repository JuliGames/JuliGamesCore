package net.juligames.core.adventure.api.prompt.msc;

import de.bentzin.conversationlib.ConversationContext;
import de.bentzin.conversationlib.prompt.Prompt;
import de.bentzin.conversationlib.prompt.ValidatingPrompt;
import de.bentzin.tools.logging.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 19.05.2023
 */
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


    //DO NOT PUSH TO VERSION CONTROL
    @TestOnly
    private static class Test {


        public static void main(String[] args) {
            final Prompt trans = TransitivePrompt.<Class<?>, Method>functional(
                    context -> Component.text("this text was removed..."),
                    context -> context.getData("class"),
                    (aClass, s) -> {
                        try {
                            return aClass.getMethod(s, (Class<?>[]) null);
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    (context, method) -> {
                        try {
                            method.invoke(context.getData("object"), (Object[]) null);
                            return END_OF_CONVERSATION;
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            ((Logger) context.getData("logger")).error("this text was removed too." + e.getMessage());
                            throw new RuntimeException(e);
                        }
                    }
            );

        }
    }
}
