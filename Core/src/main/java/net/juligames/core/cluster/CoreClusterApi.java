package net.juligames.core.cluster;

import com.hazelcast.client.Client;
import com.hazelcast.client.ClientService;
import com.hazelcast.cluster.Cluster;
import com.hazelcast.cluster.ClusterState;
import com.hazelcast.cluster.Member;
import com.hazelcast.core.HazelcastInstance;
import net.juligames.core.Core;
import net.juligames.core.api.cluster.ClusterApi;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public final class CoreClusterApi implements ClusterApi {

    @Override
    @ApiStatus.Experimental
    public UUID @NotNull [] getMembers() {
       return (UUID[]) getHazelMembers().stream().map(Member::getUuid).toArray();
    }

    public @NotNull Set<Member> getHazelMembers() {
        return getCluster().getMembers();
    }

    @Override
    public String[] getClientNames() {
       //manual converter
        Collection<Client> clients = getClients();
        String[] names = new String[clients.size()];
        int i = 0;
        for (Client client : clients) {
            names[i] = client.getName();
            i++;
        }
        return names;
    }

    @Override
    public UUID @NotNull [] getClientUUIDS() {
        //manual converter
        Collection<Client> clients = getClients();
        UUID[] names = new UUID[clients.size()];
        int i = 0;
        for (Client client : clients) {
            names[i] = client.getUuid();
            i++;
        }
        return names;
    }

    public @NotNull Collection<Client> getClients() {
        return getClientService().getConnectedClients();
    }

    public @NotNull ClientService getClientService() {
        return instance().getClientService();
    }

    private HazelcastInstance instance() {
        return Core.getInstance().getOrThrow();
    }

    public @NotNull Cluster getCluster() {
        return instance().getCluster();
    }

    public @NotNull Member getLocalMember() {
        return getCluster().getLocalMember();
    }

    public void shutdown(){
        getCluster().shutdown();
    }

    public @NotNull ClusterState getState() {
        return getCluster().getClusterState();
    }

    /**
     * @return Gives always the UUID of this instance (works for members and clients)
     */
    public UUID getLocalUUID(){
        return instance().getLocalEndpoint().getUuid();
    }


}
