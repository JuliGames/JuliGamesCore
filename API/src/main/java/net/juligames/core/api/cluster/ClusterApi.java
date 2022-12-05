package net.juligames.core.api.cluster;

import java.util.Map;
import java.util.UUID;

/**
 * @apiNote This gives you access to some limited information about hazelcast
 * @author Ture Bentzin
 * 16.11.2022
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
     * @return Gives always the UUID of this instance (works for members and clients)
     */
    UUID getLocalUUID();

    Map<UUID,InstanceType> getAllUUIDS();

    enum InstanceType{CLIENT, MEMBER}
}
