package net.juligames.core.cluster;

import com.hazelcast.client.Client;
import com.hazelcast.client.ClientService;
import com.hazelcast.cluster.Cluster;
import com.hazelcast.cluster.ClusterState;
import com.hazelcast.cluster.Member;
import com.hazelcast.core.HazelcastInstance;
import de.bentzin.tools.pair.DividedPair;
import net.juligames.core.Core;
import net.juligames.core.api.cluster.ClusterApi;
import net.juligames.core.api.cluster.ClusterClient;
import net.juligames.core.api.cluster.ClusterMember;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.SocketAddress;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public final class CoreClusterApi implements ClusterApi {

    @Override
    @ApiStatus.Experimental
    public UUID @NotNull [] getMembers() {
        return getHazelMembers().stream().map(Member::getUuid).toList().toArray(new UUID[0]);
    }

    public @NotNull Set<Member> getHazelMembers() {
        return getCluster().getMembers();
    }

    @Override
    public String @NotNull [] getClientNames() {
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

    @Override
    public Collection<DividedPair<UUID, String>> getClientPairs() {
        return getClients().stream().map(client -> new DividedPair<>(client.getUuid(), client.getName())).toList();
    }

    @Override
    public Set<ClusterClient> getClusterClients() {
        return getClients().stream().map(CoreClusterClient::ofHazelcast).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<ClusterMember> getClusterMembers() {
        return getHazelMembers().stream().map(CoreClusterMember::ofHazelcast).collect(Collectors.toUnmodifiableSet());
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

    /**
     * Returns this Hazelcast instance member.
     * <p>
     * The returned value will never be null, but it may change when local lite member is promoted to a data member
     * or when this member merges to a new cluster after split-brain detected. Returned value should not be
     * cached but instead this method should be called each time when local member is needed.
     * <p>
     * Supported only for members of the cluster, clients will throw a {@code UnsupportedOperationException}.
     *
     * @return this Hazelcast instance member
     */
    public @NotNull Member getLocalMember() throws UnsupportedOperationException {
        return getCluster().getLocalMember();
    }

    public void shutdown() {
        getCluster().shutdown();
    }

    public @NotNull ClusterState getState() {
        return getCluster().getClusterState();
    }

    /**
     * @return Gives always the UUID of this instance (works for members and clients)
     */
    public UUID getLocalUUID() {
        return instance().getLocalEndpoint().getUuid();
    }

    @Contract(" -> new")
    @Override
    public @NotNull Optional<String> getLocalName() {
        return Optional.of(instance().getName());
    }

    public SocketAddress getLocalAddress() {
        return instance().getLocalEndpoint().getSocketAddress();
    }

    @Override
    public @NotNull Map<UUID, InstanceType> getAllUUIDS() {
        HashMap<UUID, InstanceType> hashMap = new HashMap<>();
        for (UUID clientUUID : getClientUUIDS()) {
            if (hashMap.put(clientUUID, InstanceType.CLIENT) != null) {
                Core.getInstance().getCoreLogger().warning("Illegal UUID match seems like value was assigned before. Please report this issue to the developer. " +
                        "This may cause damage to the integrity of core to core communication and evaluation!");
            }
        }

        for (UUID member : getMembers()) {
            if (hashMap.put(member, InstanceType.MEMBER) != null) {
                Core.getInstance().getCoreLogger().warning("Illegal UUID match seems like value was assigned before. Please report this issue to the developer. " +
                        "This may cause damage to the integrity of core to core communication and evaluation!");
            }
        }
        return hashMap;
    }


}
