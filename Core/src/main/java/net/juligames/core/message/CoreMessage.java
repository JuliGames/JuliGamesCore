package net.juligames.core.message;

import net.juligames.core.api.jdbi.DBMessage;
import net.juligames.core.api.message.Message;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public class CoreMessage implements Message {

    private final DBMessage messageData;

    public CoreMessage(@Nullable DBMessage messageData) {
        this.messageData = messageData;
    }

    @Contract("_, _ -> new")
    public static @NotNull CoreMessage fromData(@Nullable DBMessage messageData, String messageKey) {
        if (messageData == null) {
            return new FallBackMessage(messageKey);
        } else {
            return new CoreMessage(messageData);
        }
    }

    @Deprecated
    @Contract("_ -> new")
    public static @NotNull CoreMessage fromData(@Nullable DBMessage messageData) {
        if (messageData == null) {
            return new FallBackMessage();
        } else {
            return new CoreMessage(messageData);
        }
    }

    @Override
    public DBMessage getMessageData() {
        return messageData;
    }

    @Override
    public String getMiniMessage() {
        return messageData.getMiniMessage();
    }

    @Override
    public String getPlainMessage() {
        return null; //TODO tag resolving
    }

    @Override
    public void doWithMiniMessage(@NotNull Function<String, String> actionToPerform) {
        String before = getMiniMessage();
        getMessageData().setMiniMessage(actionToPerform.apply(before));
    }

    @Override
    public Message clone() {
        return new CoreMessage(messageData.clone());
    }
}
