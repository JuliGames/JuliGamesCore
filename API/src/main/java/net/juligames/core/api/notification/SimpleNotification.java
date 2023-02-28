package net.juligames.core.api.notification;

import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public interface SimpleNotification {

    /**
     * @return Something that identifies that Notification
     */
    @NotNull String header();

    /**
     * @return The message of the Notification
     */
    @NotNull String message();
}
