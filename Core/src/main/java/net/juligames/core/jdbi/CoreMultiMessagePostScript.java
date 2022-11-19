package net.juligames.core.jdbi;

import net.juligames.core.api.message.Message;
import net.juligames.core.api.message.MessagePostScript;
import net.juligames.core.api.message.MessageRecipient;
import net.juligames.core.api.message.MultiMessagePostScript;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @apiNote every recipient got all the Messages in order as they are in the collection (if there is an order)
 * @author Ture Bentzin
 * 19.11.2022
 */
public class CoreMultiMessagePostScript implements MultiMessagePostScript {

    private final Collection<Message> messages;
    private final Collection<? extends MessageRecipient> recipients;
    private final Date timeSent;

    public CoreMultiMessagePostScript(Collection<Message> messages, Collection<? extends MessageRecipient> recipients, Date timeSent) {
        this.messages = messages;
        this.recipients = recipients;
        this.timeSent = timeSent;
    }

    public CoreMultiMessagePostScript(Collection<Message> messages, Collection<? extends MessageRecipient> recipients) {
        this.messages = messages;
        this.recipients = recipients;
        this.timeSent = Date.from(Instant.now());
    }

    @Override
    public Collection<Message> messages() {
        return messages;
    }

    @Override
    public Date timeSent() {
        return timeSent;
    }

    @Override
    public Collection<? extends MessageRecipient> recipients() {
        return recipients;
    }

    @Override
    public Collection<MessagePostScript> toSingles() {
        Collection<MessagePostScript> messagePostScripts = new ArrayList<>();
        for (MessageRecipient recipient : recipients)
            for (Message message : messages)
                messagePostScripts.add(new CoreMessagePostScript(message,recipient,timeSent));
        return messagePostScripts;
    }
}
