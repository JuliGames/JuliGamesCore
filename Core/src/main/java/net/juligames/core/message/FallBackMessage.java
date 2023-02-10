package net.juligames.core.message;

import net.juligames.core.Core;

/**
 * @author Ture Bentzin
 * 20.11.2022
 */
public class FallBackMessage extends CoreMessage {


    public FallBackMessage() {
        this("message not found!");
    }

    public FallBackMessage(final String messageKey) {
        super(new CreativeMessageBean(messageKey, Core.getInstance().getMessageApi().defaultLocale(), messageKey), replacements);

    }

}
