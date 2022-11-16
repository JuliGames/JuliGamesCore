package net.juligames.core.api.notification;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public interface NotificationApi {

    boolean registerListener(NotificationListener listener);

    NotificationSender getNotificationSender();

}
