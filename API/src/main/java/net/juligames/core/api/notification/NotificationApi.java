package net.juligames.core.api.notification;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public interface NotificationApi {

    /**
     * register a listener that will be listening to any incoming messages to this core
     *
     * @param listener the listener
     * @return if the registration was successful
     */
    @SuppressWarnings("UnusedReturnValue")
    boolean registerListener(NotificationListener listener);

    /**
     * unregister a listener
     *
     * @param listener the listener
     * @return if the removal was successful
     */
    boolean unregisterListener(NotificationListener listener);

    /**
     * @return the utility to send {@link Notification}s by yourself
     */
    NotificationSender getNotificationSender();

}
