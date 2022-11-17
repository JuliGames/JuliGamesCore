package net.juligames.core.cluster;

import com.hazelcast.client.Client;
import com.hazelcast.client.ClientService;
import com.hazelcast.cluster.Cluster;
import com.hazelcast.cluster.ClusterState;
import com.hazelcast.cluster.Member;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.transaction.TransactionOptions;
import net.juligames.core.Core;
import net.juligames.core.api.cluster.ClusterApi;
import net.juligames.core.api.err.dev.TODOException;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;
import java.util.function.IntFunction;

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
        //TODO
        throw new TODOException();
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

    public Client getLocalClient() {
        for (Client connectedClient : getClientService().getConnectedClients()) {

        }

        throw new TODOException();
    }

    public void shutdown(){
        getCluster().shutdown();
    }

    public @NotNull ClusterState getState() {
        return getCluster().getClusterState();
    }


}
