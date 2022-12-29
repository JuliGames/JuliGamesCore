package net.juligames.core.api.notification;

import de.bentzin.tools.pair.DividedPair;

import java.util.UUID;

/**
 * This can be used to notify other core instances with information and data
 *
 * @author Ture Bentzin
 * 16.11.2022
 */
public interface Notification extends SimpleNotification {
    /**
     * @return The message of this Notification
     */
    @Override
    String message();

    /**
     * @return The UUID of the sender (member) of this Notification, and the Name of the sender
     */
    DividedPair<UUID, String> sender();

    /**
     * @return The UUIDs of all the recipients of this Notification, and the names of them
     */
    DividedPair<UUID, String>[] addresses();

    /**
     * @return Something that identifies this Notification
     */
    @Override
    String header();
}
