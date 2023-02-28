package net.juligames.core.jdbi;

import net.juligames.core.api.message.Message;
import net.juligames.core.api.message.MessagePostScript;
import net.juligames.core.api.message.MessageRecipient;
import net.juligames.core.api.message.MultiMessagePostScript;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public class CoreMessagePostScript implements MessagePostScript {

    private final Message message;
    private final Date timeSent;
    private final MessageRecipient messageRecipient;

    public CoreMessagePostScript(Message message, MessageRecipient messageRecipient, Date timeSent) {
        this.message = message;
        this.timeSent = timeSent;
        this.messageRecipient = messageRecipient;
    }

    public CoreMessagePostScript(Message message, MessageRecipient messageRecipient) {
        this.message = message;
        this.messageRecipient = messageRecipient;
        this.timeSent = Date.from(Instant.now());
    }


    @Override
    public @NotNull Message message() {
        return message;
    }

    @Override
    public @NotNull Date timeSent() {
        return timeSent;
    }

    @Override
    public @NotNull MessageRecipient recipient() {
        return messageRecipient;
    }

    @Override
    public @NotNull MultiMessagePostScript toMulti() {
        return new CoreMultiMessagePostScript(List.of(message), List.of(recipient()), timeSent);
    }
}
