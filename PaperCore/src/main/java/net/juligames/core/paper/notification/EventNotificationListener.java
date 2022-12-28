package net.juligames.core.paper.notification;

import net.juligames.core.api.notification.Notification;
import net.juligames.core.api.notification.NotificationListener;
import net.juligames.core.paper.events.CoreNotificationEvent;
import org.bukkit.Bukkit;

/**
 * @author Ture Bentzin
 * 26.12.2022
 */
public class EventNotificationListener implements NotificationListener {
    @Override
    public void onNotification(Notification notification) {
        Bukkit.getPluginManager().callEvent(new CoreNotificationEvent(notification));
    }
}
