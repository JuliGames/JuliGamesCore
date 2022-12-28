package net.juligames.core.api.cluster;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;

/**
 * @apiNote This interface provides you with information about a Member in the Cluster.
 * @author Ture Bentzin
 * 27.12.2022
 */
public interface ClusterMember {

    /**
     *
     * @return <code>true</code> if this member is the local member, <code>false</code> otherwise.
     */
    boolean localMember();

    /**
     * @return <code>true</code> if this member is a lite member, <code>false</code> otherwise.
     * Lite members do not own any partition.
     */
    boolean isLiteMember();


    /**
     * @return the socket address of this member for member to member communications or unified depending on config.
     */
    InetSocketAddress getSocketAddress();

    /**
     * @return the UUID of this member.
     */
    UUID getUuid();

    /**
     * <b>This method might not be available on all native clients.</b>
     *
     * @return configured attributes for this member.
     */
    Map<String, String> getAttributes();

    /**
     * Returns the value of the specified key for this member or
     * null if value is undefined.
     *
     * @param key The key to lookup.
     * @return The value for this member key.
     */
    String getAttribute(String key);

}
