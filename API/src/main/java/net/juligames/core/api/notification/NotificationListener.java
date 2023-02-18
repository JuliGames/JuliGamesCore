package net.juligames.core.api.notification;

import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public interface NotificationListener {

    /**
     * This will be executed when a Notification arrives at this Core
     *
     * @param notification the notification
     */
    void onNotification(@NotNull Notification notification);

}
