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
     * This Method should only be used for logging to systems that do not support MiniMessage or Components
     *
     * @return the Message formatted without colors or decoration
     * @apiNote To get a {@link MiniMessageSerializer} you should install the AdventureAPI that provides an AdventureTagManager.
     * This {@link TagManager} can be inserted here.
     * @see net.juligames.core.adventure.AdventureTagManager
     */
    @SuppressWarnings("JavadocReference")
    String getPlainMessage(MiniMessageSerializer serializer);

    @Deprecated
    String getLegacyMessage(MiniMessageSerializer serializer);

    //String getANSIMessage(); //would be cool to have - maybe one day

    /**
     * Modify the data in this message
     *
     * @param actionToPerform the operation
     */
    void doWithMiniMessage(Function<String, String> actionToPerform);
}
