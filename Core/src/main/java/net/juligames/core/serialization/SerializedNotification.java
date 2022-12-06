package net.juligames.core.serialization;

import de.bentzin.tools.pair.DividedPair;
import net.juligames.core.notification.CoreNotification;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Ture Bentzin
 * 18.11.2022
 * @apiNote This is the "data-container" for a {@link CoreNotification} so it can be serialized using compactSerialization
 */
public class SerializedNotification {

    private String message;
    private String header;

    private String sender_uuid;
    private String sender_name;


    private String[] addresses_uuids;
    private String[] addresses_names;


    private SerializedNotification() {
    }

    @Contract(pure = true)
    public static @NotNull SerializedNotification serialize(@NotNull CoreNotification coreNotification) {
        SerializedNotification serializedNotification = new SerializedNotification();
        serializedNotification.sender_name = coreNotification.sender().getSecond();
        serializedNotification.sender_uuid = coreNotification.sender().getFirst().toString();

        serializedNotification.header = coreNotification.header();
        serializedNotification.message = coreNotification.message();

        //init
        DividedPair<UUID, String>[] addresses = coreNotification.addresses();
        int size = addresses.length;
        serializedNotification.addresses_names = new String[size];
        serializedNotification.addresses_uuids = new String[size];

        //fill
        for (int i = 0; i < addresses.length; i++) {
            serializedNotification.addresses_uuids[i]
                    = addresses[i].getFirst().toString();

            serializedNotification.addresses_names[i]
                    = addresses[i].getSecond();
        }

        return serializedNotification;
    }

    public String message() {
        return message;
    }

    public String header() {
        return header;
    }

    public UUID sender_uuid() {
        return UUID.fromString(sender_uuid);
    }

    public String sender_name() {
        return sender_name;
    }

    public String[] addresses_uuids() {
        return addresses_uuids;
    }

    public String[] addresses_names() {
        return addresses_names;
    }

    public CoreNotification deserialize() {
        return CoreNotification.deserialize(this);
    }
}
