package net.juligames.core;

import net.juligames.core.api.err.APIException;
import net.juligames.core.api.message.Message;
import net.juligames.core.api.message.MessageRecipient;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public class DummyMessageRecipient implements MessageRecipient {

    private APIException provideException(){
        throw new APIException("It seems like your core provides DummyMessageRecipients here. If you are the developer or maintainer of" +
                " your core then consider checking Core#setOnlineRecipientProvider(...)!");
    }

    /**
     * @return A human-readable name that defines this recipient
     */
    @Override
    public String getName() {
        throw provideException();
    }

    @Override
    public void deliver(Message message) {
        throw provideException();
    }

    @Override
    public void deliver(String miniMessage) {
        throw provideException();
    }

    @Override
    public @Nullable String supplyLocale() {
        throw provideException();
    }
}
