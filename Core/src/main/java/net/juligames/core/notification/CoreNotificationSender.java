package net.juligames.core.notification;

import net.juligames.core.api.notification.NotificationSender;
import net.juligames.core.api.notification.SimpleNotification;

import java.util.UUID;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public class CoreNotificationSender implements NotificationSender {
    @Override
    public void sendNotification(SimpleNotification notification, UUID... addresses) {
        //TODO
    }

    @Override
    public void broadcastNotification(SimpleNotification notification) {
        //TODO
    }
}
