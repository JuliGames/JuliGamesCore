package net.juligames.core.message;

import net.juligames.core.api.jdbi.DBMessage;
import net.juligames.core.api.message.Message;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public class CoreMessage implements Message {

    private final DBMessage messageData;

    public CoreMessage(DBMessage messageData) {
        this.messageData = messageData;
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
