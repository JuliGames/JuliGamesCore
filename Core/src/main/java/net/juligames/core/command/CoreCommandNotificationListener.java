package net.juligames.core.command;

import net.juligames.core.Core;
import net.juligames.core.api.notification.Notification;
import net.juligames.core.api.notification.NotificationListener;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 05.12.2022
 */
public class CoreCommandNotificationListener implements NotificationListener {
    /**
     * This will be executed when a Notification arrives at this Core
     *
     * @param notification the notification
     */
    @Override
    public void onNotification(@NotNull Notification notification) {
        if(notification.header().equals(CoreCommandApi.COMMAND_NOTIFICATION_HEADER))
            Core.getInstance().getCommandApi().handle(notification.message());
    }
}
