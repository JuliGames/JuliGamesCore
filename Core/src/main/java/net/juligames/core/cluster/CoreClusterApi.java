package net.juligames.core.cluster;

import com.hazelcast.cluster.Cluster;
import com.hazelcast.cluster.ClusterState;
import com.hazelcast.cluster.Member;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.transaction.TransactionOptions;
import net.juligames.core.Core;
import net.juligames.core.api.cluster.ClusterApi;
import net.juligames.core.api.err.dev.TODOException;
import org.jetbrains.annotations.ApiStatus;

import java.util.Set;
import java.util.UUID;
import java.util.function.IntFunction;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public class CoreClusterApi implements ClusterApi {

    @Override
    @ApiStatus.Experimental
    public UUID[] getMembers() {
       return (UUID[]) getHazelMembers().stream().map(Member::getUuid).toArray();
    }

    public Set<Member> getHazelMembers() {
        return getCluster().getMembers();
    }

    @Override
    public String[] getClientNames() {
        //TODO
        throw new TODOException();
    }

    private HazelcastInstance instance() {
        return Core.getInstance().getOrThrow();
    }

    public Cluster getCluster() {
        return instance().getCluster();
    }

    public void shutdown(){
        getCluster().shutdown();
    }

    public ClusterState getState() {
        return getCluster().getClusterState();
    }


}
