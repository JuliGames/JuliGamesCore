package net.juligames.core.notification;

import net.juligames.core.api.notification.SimpleNotification;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
class CoreSimpleNotification implements SimpleNotification {

    private final String header;
    private final String message;

    CoreSimpleNotification(String header, String message) {
        this.header = header;
        this.message = message;
    }

    CoreSimpleNotification() {
        this.header = null;
        this.message = null;
    }

    /**
     * @return Something that identifies that Notification
     */
    @Override
    public @NotNull String header() {
        return header;
    }

    /**
     * @return The message of the Notification
     */
    @Override
    public @NotNull String message() {
        return message;
    }
}
