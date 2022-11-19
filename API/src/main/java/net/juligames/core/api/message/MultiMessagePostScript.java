package net.juligames.core.api.message;

import java.util.Collection;
import java.util.Date;

/**
 * @author Ture Bentzin
 * 19.11.2022
 * @see MessagePostScript
 * @see MessageApi
 */
public interface MultiMessagePostScript {
    Collection<Message> messages();
    Date timeSent();
    Collection<? extends MessageRecipient> recipients();

    Collection<MessagePostScript> toSingles();
}
