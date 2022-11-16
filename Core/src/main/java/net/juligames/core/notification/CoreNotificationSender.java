package net.juligames.core.notification;

import com.hazelcast.client.ClientService;
import com.hazelcast.cluster.Member;
import com.hazelcast.core.HazelcastInstance;
import net.juligames.core.Core;
import net.juligames.core.api.notification.NotificationApi;
import net.juligames.core.api.notification.NotificationSender;
import net.juligames.core.api.notification.SimpleNotification;

import java.util.Set;
import java.util.UUID;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public class CoreNotificationSender implements NotificationSender {

    private CoreNotificationApi notificationApi;

    public CoreNotificationSender(CoreNotificationApi notificationApi) {
        this.notificationApi = notificationApi;
    }

    @Override
    public void sendNotification(SimpleNotification notification, UUID... addresses) {
        //TODO

        HazelcastInstance hazelcastInstance = Core.getInstance().getOrThrow();
        ClientService clientService = hazelcastInstance.getClientService();

        Set<Member> hazelMembers = Core.getInstance().getClusterApi().getHazelMembers();
        for (Member hazelMember : hazelMembers) {
            if(notificationApi.getBlacklist().contains(hazelMember.getAddress())) continue;
            //TODO: CRAFT NOTIFICATION
            hazelcastInstance.getQueue(hazelMember.getUuid().toString()).put(null);
        }
    }

    @Override
    public void broadcastNotification(SimpleNotification notification) {
        //TODO
    }


}
