package net.juligames.core.notification;

import com.hazelcast.client.Client;
import com.hazelcast.client.ClientService;
import com.hazelcast.cluster.Address;
import com.hazelcast.cluster.Member;
import com.hazelcast.core.HazelcastInstance;
import de.bentzin.tools.pair.DividedPair;
import net.juligames.core.Core;
import net.juligames.core.api.notification.NotificationSender;
import net.juligames.core.api.notification.SimpleNotification;

import java.util.*;

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
        final ArrayList<DividedPair<UUID,String>> addressPairs = new ArrayList<>();

        //check Members
        for (UUID address : addresses) {
            for (Member hazelMember : hazelMembers) {
                if(hazelMember.getUuid().equals(address)){
                    Address ad = hazelMember.getAddress();
                    String valueName = hazelMember.getAttribute("name");
                    addressPairs.add(new DividedPair<>(address, valueName != null? valueName : ad.toString()));
                }
            }
        }

        //check Clients

        for (UUID address : addresses) {
            Collection<Client> clients = Core.getInstance().getClusterApi().getClients();
            for (Client client : clients) {
                if(client.getUuid().equals(address)){
                    addressPairs.add(new DividedPair<>(address,client.getName()));
                }
            }
        }

        @SuppressWarnings("unchecked")
        DividedPair<UUID,String>[] dividedPairs = new DividedPair[addressPairs.size()];
        for (int i = 0; i < addressPairs.size(); i++) {
            dividedPairs[i] = addressPairs.get(i);
        }

        CoreNotification coreNotification = CoreNotification.craft(notification, us, dividedPairs);
        //now finally time to send the dude
        Core.getInstance().getNotificationCore().sendNotification(coreNotification);

    }


    @Override
    public void broadcastNotification(SimpleNotification notification) {
        //members first
        ArrayList<UUID> uuids = new ArrayList<>();
        Collections.addAll(uuids, Core.getInstance().getClusterApi().getMembers());
        Collections.addAll(uuids,Core.getInstance().getClusterApi().getClientUUIDS());
        //legacy
        UUID[] uuids1 = new UUID[uuids.size()];
        for (int i = 0; i < uuids.size(); i++) {
            uuids1[i] = uuids.get(i);
        }
        sendNotification(notification,uuids1);
    }

    @Override
    public void sendNotification(String header, String message, UUID... addressees) {
        sendNotification(new CoreSimpleNotification(header,message),addressees);
    }

    @Override
    public void broadcastNotification(String header, String message) {
        broadcastNotification(new CoreSimpleNotification(header,message));
    }


}