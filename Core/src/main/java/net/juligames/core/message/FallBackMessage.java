package net.juligames.core.message;

import net.juligames.core.Core;
import org.jetbrains.annotations.ApiStatus;

/**
 * @author Ture Bentzin
 * 20.11.2022
 */
public class FallBackMessage extends CoreMessage {


    @ApiStatus.Internal
    public FallBackMessage() {
        this("null");
    }

    public FallBackMessage(final String messageKey) {
        super(new CreativeMessageBean(messageKey, Core.getInstance().getMessageApi().defaultLocale(), messageKey));
    }

}
