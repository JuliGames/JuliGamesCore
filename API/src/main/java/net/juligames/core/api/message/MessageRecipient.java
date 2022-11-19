package net.juligames.core.api.message;

import org.jetbrains.annotations.ApiStatus;

/**
 * @author Ture Bentzin
 * 18.11.2022
 */
public interface MessageRecipient {

    /**
     * @return A human-readable name that defines this recipient
     */
    String getName();
    void deliver(Message message);

    @ApiStatus.Internal
    @Deprecated
    /**
     * delivers a miniMessage string to the recipient
     *
     */
    void deliver(String miniMessage);
}
