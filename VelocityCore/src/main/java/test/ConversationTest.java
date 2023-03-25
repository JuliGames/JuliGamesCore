package test;

import de.bentzin.conversationlib.ConversationContext;
import de.bentzin.conversationlib.manager.ConversationManager;
import de.bentzin.conversationlib.prompt.BooleanPrompt;
import de.bentzin.conversationlib.prompt.LambdaPrompt;
import de.bentzin.conversationlib.prompt.Prompt;
import net.juligames.core.adventure.api.MessageRepresentation;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
public class ConversationTest {
    public static void demo(){

        ConversationManager conversationManager = new ConversationManager();
        Audience player = Audience.empty();

        conversationManager.newBuilder().prefix( context -> Component.text("> "))
                .firstPrompt(
                        new LambdaPrompt(conversationContext -> Component.text("Bitte Name angeben:"),
                                conversationContext -> true,
                                (conversationContext, s) -> {
                                    conversationContext.setData("name",s);
                                    return new BooleanPrompt() {
                                        @Override
                                        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, boolean input) {
                                            return MessageRepresentation.;
                                        }

                                        @Override
                                        public @NotNull ComponentLike getPromptMessage(@NotNull ConversationContext conversationContext) {
                                            return MessageRepresentation.represent("ley.key", conversationContext.target());
                                        }
                                    }
                                }))

    }
}
