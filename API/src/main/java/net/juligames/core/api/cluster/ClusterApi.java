package net.juligames.core.api.cluster;

import java.util.UUID;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public interface ClusterApi {

    UUID[] getMembers();

    String[] getClientNames();

    UUID[] getClientUUIDS();

    /**
     * @return Gives always the UUID of this instance (works for members and clients)
     */
    UUID getLocalUUID();
}
