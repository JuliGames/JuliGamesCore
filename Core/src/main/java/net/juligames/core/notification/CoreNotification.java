package net.juligames.core.notification;

import de.bentzin.tools.pair.DividedPair;
import net.juligames.core.api.notification.SimpleNotification;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public class CoreNotification implements net.juligames.core.api.notification.Notification {

    @Contract("_, _, _ -> new")
    public static @NotNull CoreNotification craft(@NotNull SimpleNotification notification, DividedPair<UUID,String> sender, DividedPair<UUID,String>[] addresses) {
        return new CoreNotification(notification.message(),notification.header(),sender,addresses);
    }

    private final String message;
    private final String header;

    private final DividedPair<UUID,String> sender;
    private final DividedPair<UUID,String>[] addresses;

    public CoreNotification(String message, String header, DividedPair<UUID, String> sender, DividedPair<UUID, String>[] addresses) {
        this.message = message;
        this.header = header;
        this.sender = sender;
        this.addresses = addresses;
    }

    /**
     * @return The message of this Notification
     */
    @Override
    public String message() {
        return message;
    }

    /**
     * @return The UUID of the sender (member) of this Notification, and the Name of the sender
     */
    @Override
    public DividedPair<UUID, String> sender() {
        return sender;
    }

    /**
     * @return The UUIDs of all the recipients of this Notification, and the names of them
     */
    @Override
    public DividedPair<UUID, String>[] addresses() {
        return addresses;
    }

    /**
     * @return Something that identifies that Notification
     */
    @Override
    public String header() {
        return header;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CoreNotification{");
        sb.append(", header='").append(header).append('\'');
        sb.append("message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
