package net.juligames.core.notification;

import de.bentzin.tools.pair.DividedPair;
import net.juligames.core.api.notification.SimpleNotification;
import net.juligames.core.serialization.SerializedNotification;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
@SuppressWarnings({"unchecked", "ClassCanBeRecord"})
public class CoreNotification implements net.juligames.core.api.notification.Notification {

    private final String message;
    private final String header;
    private final DividedPair<UUID, String> sender;
    private final DividedPair<UUID, String>[] addresses;

    public CoreNotification(String message, String header, DividedPair<UUID, String> sender, DividedPair<UUID, String>[] addresses) {
        this.message = message;
        this.header = header;
        this.sender = sender;
        this.addresses = addresses;
    }

    @Contract("_, _, _ -> new")
    public static @NotNull CoreNotification craft(@NotNull SimpleNotification notification, DividedPair<UUID, String> sender, DividedPair<UUID, String>[] addresses) {
        return new CoreNotification(notification.message(), notification.header(), sender, addresses);
    }

    @Contract("_ -> new")
    public static @NotNull CoreNotification deserialize(@NotNull SerializedNotification serializedNotification) {
        int length = serializedNotification.addresses_names().length;
        assert length == serializedNotification.addresses_uuids().length;

        DividedPair<UUID, String> sender = new DividedPair<>(
                serializedNotification.sender_uuid(), serializedNotification.sender_name());

        DividedPair<UUID, String>[] addresses = new DividedPair[length];
        for (int i = 0; i < addresses.length; i++) {
            addresses[i] = new DividedPair<>(
                    UUID.fromString(serializedNotification.addresses_uuids()[i]),
                    serializedNotification.addresses_names()[i]
            );
        }

        return new CoreNotification(serializedNotification.message(),
                serializedNotification.header(),
                sender, addresses);
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
        return "CoreNotification{" + ", header='" + header + '\'' +
                "message='" + message + '\'' +
                '}';
    }

    public SerializedNotification serialize() {
        return SerializedNotification.serialize(this);
    }
}
