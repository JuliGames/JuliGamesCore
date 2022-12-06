package net.juligames.core.api.message;

import net.juligames.core.api.jdbi.DBMessage;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 18.11.2022
 */
public interface Message extends Cloneable {

    /**
     * @return the data container of this Message
     */
    @ApiStatus.Internal
    DBMessage getMessageData();

    /**
     * @return the miniMessage (unresolved obviously)
     */
    String getMiniMessage();

    /**
     * @return the Message formatted without colors or decoration
     */
    String getPlainMessage();

    /**
     * Modify the data in this message
     *
     * @param actionToPerform the operation
     */
    void doWithMiniMessage(Function<String, String> actionToPerform);
}
