package net.juligames.core.cluster;

import net.juligames.core.api.cluster.ClusterApi;
import net.juligames.core.api.err.dev.TODOException;

import java.util.UUID;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public class CoreClusterApi implements ClusterApi {
    @Override
    public UUID[] getMembers() {
        //TODO
        throw new TODOException();
    }

    @Override
    public String[] getClientNames() {
        //TODO
        throw new TODOException();
    }
}
