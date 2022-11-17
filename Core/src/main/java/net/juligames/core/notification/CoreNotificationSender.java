package net.juligames.core.notification;

import com.hazelcast.client.ClientService;
import com.hazelcast.cluster.Member;
import com.hazelcast.core.HazelcastInstance;
import de.bentzin.tools.pair.DividedPair;
import net.juligames.core.Core;
import net.juligames.core.api.notification.Notification;
import net.juligames.core.api.notification.NotificationApi;
import net.juligames.core.api.notification.NotificationSender;
import net.juligames.core.api.notification.SimpleNotification;
import org.checkerframework.checker.units.qual.C;

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

        final HazelcastInstance hazelcastInstance = Core.getInstance().getOrThrow();
        final ClientService clientService = hazelcastInstance.getClientService();
        final Set<Member> hazelMembers = Core.getInstance().getClusterApi().getHazelMembers();

        final Member localMember = Core.getInstance().getClusterApi().getLocalMember();

        final DividedPair<UUID,String> us = new DividedPair<>(localMember.getUuid(),"TODO");
        final DividedPair<UUID,String>[] addressPairs = new DividedPair[addresses.length];

        CoreNotification coreNotification = CoreNotification.craft(notification, us, addressPairs);
        for (Member hazelMember : hazelMembers) {
            if(notificationApi.getBlacklist().contains(hazelMember.getAddress())) continue;
            //TODO: CRAFT NOTIFICATION
            if(notification instanceof Notification implication) {
                return;
            }

           // hazelMembers.stream().filter(member -> member.getUuid().equals())

        }
    }


    @Override
    public void broadcastNotification(SimpleNotification notification) {
        //TODO
    }


}
