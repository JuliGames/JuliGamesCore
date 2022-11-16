package net.juligames.core.api.cluster;

import java.util.UUID;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public interface ClusterApi {

    UUID[] getMembers();

    String[] getClientNames();
}
