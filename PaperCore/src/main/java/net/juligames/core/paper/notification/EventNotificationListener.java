package net.juligames.core.paper.notification;

import net.juligames.core.api.notification.Notification;
import net.juligames.core.api.notification.NotificationListener;
import net.juligames.core.paper.PaperCorePlugin;
import net.juligames.core.paper.events.CoreNotificationEvent;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 26.12.2022
 */
public class EventNotificationListener implements NotificationListener {
    @Override
    public void onNotification(@NotNull Notification notification) {
        Bukkit.getScheduler().runTask(PaperCorePlugin.getPlugin(),
                () ->  Bukkit.getPluginManager().callEvent(new CoreNotificationEvent(notification)));
    }
}
