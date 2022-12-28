package net.juligames.core.api.cluster;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.UUID;

/**
 * The ClusterClient interface allows to get information about
 * a connected client's socket address, type and UUID.
 *
 * @author Ture Bentzin
 * 27.12.2022
 * @apiNote Never try to modify data here
 */
public interface ClusterClient {


    /**
     * Returns a unique UUID for this client.
     *
     * @return a unique UUID for this client
     */
    UUID getUuid();

    /**
     * Returns the socket address of this client.
     *
     * @return the socket address of this client
     */
    InetSocketAddress getSocketAddress();

    /**
     * Type could be a client type from {@link com.hazelcast.internal.nio.ConnectionType} or
     * it can be a custom client implementation with a name outside of this @{link ConnectionType}
     *
     * @return the type of this client
     */
    String getClientType();


    /**
     * This method may return null depending on the client version and the client type
     * Java client provides client name starting with 3.12
     *
     * @return the name of this client if provided, null otherwise
     */
    String getName();

    /**
     * @return read only set of all labels of this client.
     */
    Set<String> getLabels();
}
