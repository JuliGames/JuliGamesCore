package net.juligames.core.adventure.api.prompt;

import de.bentzin.conversationlib.ConversationContext;
import de.bentzin.conversationlib.prompt.MessagePrompt;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * This prompt only displays a single line that defaults to " ".
 * You can set "spaceprompt.text" (in {@link ConversationContext#sessionData()}) to a {@link String} or a {@link ComponentLike}
 * - if you do this the default message will be replaced to the content of "spaceprompt.text"
 *
 * @author Ture Bentzin
 * 14.04.2023
 */
@ApiStatus.AvailableSince("1.6")
public abstract class SpacePrompt extends MessagePrompt {

    @Override
    public final @NotNull ComponentLike getPromptMessage(@NotNull ConversationContext conversationContext) {
        final Object object = conversationContext.getData("spaceprompt.text");
        return object instanceof ComponentLike componentLike ? componentLike : Component.text(object instanceof String plaintext ? plaintext : " ");
    }
}
