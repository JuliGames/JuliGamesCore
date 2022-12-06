package net.juligames.core.notification;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.MessageListener;
import de.bentzin.tools.pair.DividedPair;
import net.juligames.core.serialization.SerializedNotification;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author Ture Bentzin
 * 17.11.2022
 */
public class TopicNotificationCore {

    private final HazelcastInstance hazelcastInstance;

    public TopicNotificationCore(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    public void sendNotification(@NotNull CoreNotification coreNotification) {
        List<DividedPair<UUID, String>> dividedPairs = Arrays.stream(coreNotification.addresses()).toList(); //copy data
        for (DividedPair<UUID, String> addressee : dividedPairs) {
            UUID uuid = addressee.getFirst();
            hazelcastInstance.<SerializedNotification>getTopic("notify: " + uuid.toString())
                    .publish(coreNotification.serialize());
        }
    }

    public void subscribeOnUUID(@NotNull UUID uuid, MessageListener<SerializedNotification> notificationListener) {
        hazelcastInstance.<SerializedNotification>getTopic("notify: " + uuid.toString()).
                addMessageListener(notificationListener);
    }
}
