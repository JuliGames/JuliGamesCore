package net.juligames.core.api.message;

import java.util.Date;

/**
 * @author Ture Bentzin
 * 18.11.2022
 */
public interface MessagePostScript {

    /**
     * @return the message that was sent (this may be not the exact message)
     */
    Message message();

    /**
     * @return the time (not exact - do not use for encryption) the message sending was complete
     */
    Date timeSent();

    /**
     * @return the recipient of the message
     */
    MessageRecipient recipient();

    /**
     * @return transfer this {@link MessagePostScript} to a {@link MultiMessagePostScript}
     */
    MultiMessagePostScript toMulti();
}
