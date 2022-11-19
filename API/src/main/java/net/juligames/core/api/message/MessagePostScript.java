package net.juligames.core.api.message;

import java.util.Date;

/**
 * @author Ture Bentzin
 * 18.11.2022
 */
public interface MessagePostScript {

    Message message();
    Date timeSent();
    MessageRecipient recipient();

    MultiMessagePostScript toMulti();
}
