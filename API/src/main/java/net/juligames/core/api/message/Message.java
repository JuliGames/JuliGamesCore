package net.juligames.core.api.message;

import net.juligames.core.api.jdbi.DBMessage;
import net.juligames.core.api.jdbi.MessageDAO;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 18.11.2022
 */
public interface Message extends Cloneable {

    @ApiStatus.Internal
    DBMessage getMessageData();

    String getMiniMessage();
    String getPlainMessage();

    void doWithMiniMessage(Function<String, String> actionToPerform);
}
