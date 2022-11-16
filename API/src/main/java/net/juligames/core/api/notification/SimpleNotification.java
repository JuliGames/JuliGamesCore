package net.juligames.core.api.notification;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public interface SimpleNotification {

    /**
     * @return Something that identifies that Notification
     */
    String header();

    /**
     * @return The message of the Notification
     */
    String message();
}
