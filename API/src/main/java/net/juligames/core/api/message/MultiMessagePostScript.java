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
    /**
     * @return A Collection of the messages sent to every recipient (the messages might not be exact)
     */
    Collection<Message> messages();

    /**
     * @return A Collection of the messages sent to every recipient
     */
    Date timeSent();

    /**
     * @return A Collection of the recipients that got all of the messages
     */
    Collection<? extends MessageRecipient> recipients();

    /**
     * convert this {@link MultiMessagePostScript} to a {@link Collection} of {@link MessagePostScript}s
     */
    Collection<MessagePostScript> toSingles();
}
