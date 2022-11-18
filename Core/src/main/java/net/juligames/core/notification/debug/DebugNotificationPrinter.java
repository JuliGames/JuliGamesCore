package net.juligames.core.notification.debug;

import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;
import de.bentzin.tools.logging.Logger;
import net.juligames.core.Core;
import net.juligames.core.api.notification.Notification;
import net.juligames.core.notification.CoreNotification;
import net.juligames.core.serialization.SerializedNotification;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author Ture Bentzin
 * 17.11.2022
 */
public class DebugNotificationPrinter implements MessageListener<SerializedNotification> {

    public static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    /**
     * Invoked when a message is received for the topic. Note that the topic guarantees message ordering.
     * Therefore there is only one thread invoking onMessage. The user should not keep the thread busy, but preferably
     * should dispatch it via an Executor. This will increase the performance of the topic.
     *
     * @param message the message that is received for the topic
     */
    @Override
    public void onMessage(@NotNull Message<SerializedNotification> message) {
        Logger notify = Core.getInstance().getCoreLogger().adopt("notify");
        notify.info("----------------NEW MESSAGE----------------");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(message.getPublishTime()), ZoneId.systemDefault());
        notify.info("Message sent: " + FORMATTER.format(localDateTime));
        notify.info("Sender (Member): " + message.getPublishingMember().getUuid());
        Notification notification = message.getMessageObject().deserialize();
        notify.info("Notification Header: " + notification.header());
        notify.info("Notification Content: " + notification.message());
        notify.info("--------------->NEW MESSAGE<---------------");
    }
}
