package net.juligames.core.api.notification;

import java.util.UUID;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public interface NotificationSender {
    void sendNotification(SimpleNotification notification, UUID... addresses);
    void broadcastNotification(SimpleNotification notification);

    void sendNotification(String header, String message, UUID... addressees);
    void broadcastNotification(String header, String message);
}
