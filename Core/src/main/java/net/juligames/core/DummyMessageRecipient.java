package net.juligames.core;

import net.juligames.core.api.err.APIException;
import net.juligames.core.api.message.Message;
import net.juligames.core.api.message.MessageRecipient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This can be used to fill a needed recipient parameter as fallback. On execution of methods for this recipient execptions will
 * be thrown with a small explanation of the issue.
 *
 * @author Ture Bentzin
 * 19.11.2022
 */
public class DummyMessageRecipient implements MessageRecipient {

    private APIException provideException() {
        throw new APIException("It seems like your core provides DummyMessageRecipients here. If you are the developer or maintainer of" +
                " your core then consider checking Core#setOnlineRecipientProvider(...)!");
    }

    private APIException provideException(String deliveryAttempt) {
        throw new APIException("It seems like your core provides DummyMessageRecipients here. If you are the developer or maintainer of" +
                " your core then consider checking Core#setOnlineRecipientProvider(...)! " + " An attempt was made to" +
                "send the following message: " + deliveryAttempt);
    }

    /**
     * @return A human-readable name that defines this recipient
     */
    @Override
    public @NotNull String getName() {
        throw provideException();
    }

    @Override
    public void deliver(@NotNull Message message) {
        throw provideException(message.getMiniMessage());
    }

    @Override
    public void deliver(@NotNull String miniMessage) {
        throw provideException(miniMessage);
    }

    @Override
    public @Nullable String supplyLocale() {
        throw provideException();
    }
}
