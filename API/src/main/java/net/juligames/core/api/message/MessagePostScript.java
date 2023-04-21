package net.juligames.core.api.message;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

/**
 * @author Ture Bentzin
 * 18.11.2022
 */
public interface MessagePostScript {

    /**
     * Gets the message that was sent.
     *
     * @return the message that was sent (this may be not the exact message)
     */
    @NotNull Message message();

    /**
     * Gets the time the message was sent.
     *
     * @return the time (not exact - do not use for encryption) the message sending was complete
     */
    @NotNull Date timeSent();

    /**
     * Gets the recipient of the message.
     *
     * @return the recipient of the message
     */
    @NotNull MessageRecipient recipient();

    /**
     * Converts this {@link MessagePostScript} to a {@link MultiMessagePostScript}.
     *
     * @return the {@link MultiMessagePostScript} representation of this object
     */
    @NotNull MultiMessagePostScript toMulti();
}


