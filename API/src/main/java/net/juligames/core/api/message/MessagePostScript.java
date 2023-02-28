package net.juligames.core.api.message;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

/**
 * @author Ture Bentzin
 * 18.11.2022
 */
public interface MessagePostScript {

    /**
     * @return the message that was sent (this may be not the exact message)
     */
    @NotNull Message message();

    /**
     * @return the time (not exact - do not use for encryption) the message sending was complete
     */
    @NotNull Date timeSent();

    /**
     * @return the recipient of the message
     */
    @NotNull MessageRecipient recipient();

    /**
     * @return transfer this {@link MessagePostScript} to a {@link MultiMessagePostScript}
     */
    @NotNull MultiMessagePostScript toMulti();
}
