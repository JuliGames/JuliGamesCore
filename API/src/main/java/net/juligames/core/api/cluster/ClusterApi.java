package net.juligames.core.api.cluster;

import de.bentzin.tools.pair.BasicPair;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

/**
 * @author Ture Bentzin
 * 16.11.2022
 * @apiNote This gives you access to some limited information about hazelcast.
 */
public interface ClusterApi {

    /**
     * @return an Array of the UUIDs from the Members in this hazelcast cluster
     */
    UUID[] getMembers();

    /**
     * @return an Array of the names from the clients connected to the hazelcast cluster
     */
    String[] getClientNames();

    /**
     * @return an Array of the UUIDs from the clients connected to the hazelcast cluster
     */
    UUID[] getClientUUIDS();

    /**
     * This contains the same data then {@link ClusterApi#getClientNames()} & {@link ClusterApi#getClientUUIDS()}
     *
     * @return a collection of pairs that identify a Client by his {@link UUID} and name
     */
    Collection<? extends BasicPair<UUID, String>> getClientPairs();

    /**
     * This returns a {@link Set} of the Clients in the Cluster
     * The Object used here to contain the client information is a custom implementation for this api and NOT SUPPORTED BY HAZELCAST
     *
     * @return a {@link Set} of {@link ClusterClient}s
     */
    @ApiStatus.Experimental
    Set<ClusterClient> getClusterClients();

    /**
     * This returns a {@link Set} of the Members in the Cluster
     * The Object used here to contain the client information is a custom implementation for this api and NOT SUPPORTED BY HAZELCAST
     *
     * @return a {@link Set} of {@link ClusterMember}s
     */
    @ApiStatus.Experimental
    Set<ClusterMember> getClusterMembers();

    /**
     * @return Gives always the UUID of this instance (works for members and clients)
     */
    UUID getLocalUUID();

    /**
     * This will return an {@link Optional} containing the name of the local Hazelcast Instance if present. Otherwise
     * the Optional will be empty
     */
    Optional<String> getLocalName();

    Map<UUID, InstanceType> getAllUUIDS();

    enum InstanceType {CLIENT, MEMBER}
}
