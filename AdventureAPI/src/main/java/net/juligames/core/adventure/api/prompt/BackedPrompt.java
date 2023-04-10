package net.juligames.core.adventure.api.prompt;

import de.bentzin.conversationlib.ConversationContext;
import de.bentzin.conversationlib.prompt.Prompt;
import de.bentzin.conversationlib.prompt.ValidatingPrompt;
import de.bentzin.tools.misc.SubscribableType;
import net.juligames.core.api.config.Interpreter;
import net.juligames.core.api.misc.APIUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 10.04.2023
 */
public abstract class BackedPrompt<V> extends ValidatingPrompt {

    private final @NotNull PromptBacker<V> promptBacker;

    public BackedPrompt(@NotNull PromptBacker<V> promptBacker) {
        this.promptBacker = promptBacker;
    }

    protected @NotNull PromptBacker<V> getPromptBacker() {
        return promptBacker;
    }

    protected boolean isInputValid(@NotNull ConversationContext context, @NotNull String input) {
        return promptBacker.canProvide(input);
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
        return acceptIndexBackedInput(context, promptBacker.provide(input));
    }

    public abstract @Nullable Prompt acceptIndexBackedInput(@NotNull ConversationContext context, @NotNull V v);

    public interface PromptBacker<V> {

        static <V> @NotNull PromptBacker<V> fromFunction(@NotNull Function<String, V> function) {
            return new SimplePromptBacker<V>() {
                @Override
                public boolean canProvide(@NotNull String string) {
                    return provideOrNull(string) != null;
                }

                @Override
                public @Nullable V provideOrNull(@NotNull String string) {
                    return function.apply(string);
                }
            };
        }

        static <V> @NotNull PromptBacker<V> fromInterpreter(@NotNull Interpreter<V> vInterpreter) {
            return new SimplePromptBacker<V>() {
                @Override
                public boolean canProvide(@NotNull String string) {
                    return APIUtils.executedWithoutException(() -> vInterpreter.interpret(string));
                }

                @Override
                public @NotNull V provideOrNull(@NotNull String string) {
                    try {
                        return vInterpreter.interpret(string);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        }

        boolean canProvide(@NotNull String string);

        @NotNull
        V provide(@NotNull String string) throws NoSuchElementException;

        @Nullable
        V provideOrNull(@NotNull String string);

        @NotNull
        V provideOr(@NotNull String string, @NotNull V v);

        @NotNull
        Optional<V> asOptional(@NotNull String string);

        @NotNull
        SubscribableType<V> asType(@NotNull String string);
    }

    /**
     * A PromptBacker that works backed from the #provideOrNull(String)
     *
     * @param <V> v
     */
    public abstract static class SimplePromptBacker<V> implements PromptBacker<V> {

        @Override
        public abstract boolean canProvide(@NotNull String string);

        @Override
        public abstract @Nullable V provideOrNull(@NotNull String string);

        @Override
        public @NotNull V provide(@NotNull String string) throws NoSuchElementException {
            boolean b;
            try {
                b = canProvide(string);
            } catch (Exception e) {
                throw new NoSuchElementException("wasn't able to provide because of a failed #canProvide(String) check", e);
            }
            if (!b) {
                throw new NoSuchElementException("wasn't able to provide for string: " + string);
            } else {
                try {
                    final V provideOrNull = provideOrNull(string);

                    if (provideOrNull == null) {
                        throw new NoSuchElementException("Missmatch!! #canProvide indicated providing for " + string
                                + " was possible, but #provideOrNull returned null! NAG AUTHOR!!");
                    } else {
                        //oh, finally
                        return provideOrNull;
                    }

                } catch (Exception e) {
                    throw new NoSuchElementException("wasn't able to provide because of failed #provideOrNull(String)", e);
                }

            }
        }

        @Override
        public @NotNull V provideOr(@NotNull String string, @NotNull V v) {
            return canProvide(string) ? or(this::provideOrNull, string, v) : v;
        }

        @Override
        public @NotNull Optional<V> asOptional(@NotNull String string) {
            return Optional.ofNullable(provideOrNull(string));
        }

        @Override
        public @NotNull SubscribableType<V> asType(@NotNull String string) {
            SubscribableType<V> vSubscribableType = new SubscribableType<>();
            if (canProvide(string)) {
                vSubscribableType.set(provideOrNull(string));
            }
            return vSubscribableType;
        }

        private @NotNull V or(@NotNull Function<String, V> function, @NotNull String string, @NotNull V backup) {
            @NotNull V apply = function.apply(string);
            if (apply == null) {
                return backup;
            }
            return apply;
        }

    }
}
