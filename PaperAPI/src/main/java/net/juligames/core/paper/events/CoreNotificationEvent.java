package net.juligames.core.paper.events;

import net.juligames.core.api.notification.Notification;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 26.12.2022
 */
public class CoreNotificationEvent extends Event {

    public static final @NotNull HandlerList handlerList = new HandlerList();

    private final @NotNull Notification notification;

    public CoreNotificationEvent(@NotNull Notification notification) {
        this.notification = notification;
    }

    public @NotNull Notification getNotification() {
        return notification;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
