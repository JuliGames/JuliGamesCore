package net.juligames.core.api.notification;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Ture Bentzin
 * 16.11.2022
 * @apiNote please care about the reserved headers. To look them up please check out the manual and documentation of your core and your other addons!!
 */
public interface NotificationSender {
    void sendNotification(@NotNull SimpleNotification notification, @NotNull UUID @NotNull ... addresses);

    void broadcastNotification(@NotNull SimpleNotification notification);

    void sendNotification(@NotNull String header, @NotNull String message, @NotNull UUID @NotNull ... addressees);

    void broadcastNotification(@NotNull String header, @NotNull String message);
}
